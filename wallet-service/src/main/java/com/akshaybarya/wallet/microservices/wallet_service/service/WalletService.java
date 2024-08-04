package com.akshaybarya.wallet.microservices.wallet_service.service;

import com.akshaybarya.wallet.microservices.wallet_service.constants.PG_TRANSACTION_STATE;
import com.akshaybarya.wallet.microservices.wallet_service.constants.TRANSACTION_STATE;
import com.akshaybarya.wallet.microservices.wallet_service.constants.TRANSACTION_TYPE;
import com.akshaybarya.wallet.microservices.wallet_service.data.CreateOrderDto;
import com.akshaybarya.wallet.microservices.wallet_service.data.OrderDetailsDto;
import com.akshaybarya.wallet.microservices.wallet_service.data.WalletData;
import com.akshaybarya.wallet.microservices.wallet_service.data.WalletTransaction;
import com.akshaybarya.wallet.microservices.wallet_service.helpers.UniqueIdGenerator;
import com.akshaybarya.wallet.microservices.wallet_service.proxy.PaymentServiceProxy;
import com.akshaybarya.wallet.microservices.wallet_service.repository.LedgerRepository;
import com.akshaybarya.wallet.microservices.wallet_service.repository.WalletRepository;
import com.akshaybarya.wallet.microservices.wallet_service.service.kafka.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class WalletService {
    private static final Logger log = LoggerFactory.getLogger(WalletService.class);
    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private PaymentServiceProxy paymentServiceProxy;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private KafkaService kafkaService;

    private final UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator("DEP", 12);

    public WalletTransaction getTransactionDetails(String transactionId) {
        return ledgerRepository.findByTransactionId(transactionId);
    }

    public WalletTransaction createTransaction(WalletTransaction orderDto, String userId) throws Exception {
        var wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            throw new Exception("Wallet Inactive for this user");
        }

        var orderId = uniqueIdGenerator.generateANewId();

        log.info("Creating Transaction At PG service for -> {}", orderId);

        var createOrderResponse = paymentServiceProxy.createPaymentOrder(new CreateOrderDto(orderDto.getAmount(), orderId));

        log.info("Creating Transaction response from PG service for -> {}", createOrderResponse.getBody());

        if (createOrderResponse.hasBody() && createOrderResponse.getBody() != null) {
            orderDto.setTransactionId(orderId);
            orderDto.setPgReferenceId(createOrderResponse.getBody().getTransactionId());
            orderDto.setWallet(wallet);
            ledgerRepository.save(orderDto);
            return orderDto;
        }

        throw new Exception("Unable To Create Order");
    }

    private TRANSACTION_STATE reducePgTransactionStateToTransactionType(PG_TRANSACTION_STATE pgTxnState) {
        return switch (pgTxnState) {
            case CREATED -> TRANSACTION_STATE.PENDING;
            case FAILED -> TRANSACTION_STATE.FAILED;
            case SUCCESS -> TRANSACTION_STATE.SUCCESS;
        };
    }

    private WalletTransaction fetchTransactionStatusFromPG(WalletTransaction transactionDetails) throws Exception {
        var checkOrderStatusResponse = paymentServiceProxy.getPaymentOrderDetails(transactionDetails.getPgReferenceId());

        if (checkOrderStatusResponse.hasBody() && checkOrderStatusResponse.getBody() != null) {
            var pgOrderStatusResponse = checkOrderStatusResponse.getBody();
            if (pgOrderStatusResponse.getTransactionState() != PG_TRANSACTION_STATE.CREATED) {
                transactionDetails.setTransactionState(reducePgTransactionStateToTransactionType(pgOrderStatusResponse.getTransactionState()));
                transactionDetails.setCompletedAt(pgOrderStatusResponse.getCompletedAt());
                ledgerRepository.save(transactionDetails);
                return transactionDetails;
            }
        }

        throw new Exception("Unable to fetch Order Status from PG");
    }

    public WalletTransaction checkTransactionStatus(String transactionId) throws Exception {
        var transactionDetails = ledgerRepository.findByTransactionId(transactionId);

        if (transactionDetails != null) {
            if (transactionDetails.getTransactionState() != TRANSACTION_STATE.PENDING) {
                return transactionDetails;
            }

            return fetchTransactionStatusFromPG(transactionDetails);
        }

        throw new Exception("Invalid Transaction Id");
    }

    private void updateWalletBalance(WalletTransaction walletTransaction) {
        var wallet = walletRepository.findByWalletId(walletTransaction.getWallet().getWalletId());

        if (walletTransaction.getTransactionState() != TRANSACTION_STATE.SUCCESS) {
            return;
        }

        if (walletTransaction.getTransactionType() == TRANSACTION_TYPE.DEPOSIT) {
            wallet.setRunningBalance(wallet.getRunningBalance().add(BigInteger.valueOf(walletTransaction.getAmount())));
            walletRepository.save(wallet);
        } else {
            wallet.setRunningBalance(wallet.getRunningBalance().subtract(BigInteger.valueOf(walletTransaction.getAmount())));
            walletRepository.save(wallet);
        }
    }

    public WalletTransaction updateTransactionStatus(OrderDetailsDto pgOrderStatusResponse) throws Exception {
        log.info("Webhook Received -> {}" ,pgOrderStatusResponse);
        var transactionId = pgOrderStatusResponse.getOrderId();
        var transactionDetails = ledgerRepository.findByTransactionId(transactionId);
        if (pgOrderStatusResponse.getTransactionState() != PG_TRANSACTION_STATE.CREATED) {
            transactionDetails.setTransactionState(reducePgTransactionStateToTransactionType(pgOrderStatusResponse.getTransactionState()));
            transactionDetails.setCompletedAt(pgOrderStatusResponse.getCompletedAt());
            ledgerRepository.save(transactionDetails);
            return transactionDetails;
        }

        throw new Exception("Invalid Order Details");
    }

    public void updateTransactionStatusAndBalance(OrderDetailsDto orderDetailsDto) {
        try {
            var transactionDetails = updateTransactionStatus(orderDetailsDto);
            updateWalletBalance(transactionDetails);
            kafkaService.updateWalletBalance(walletRepository.findByWalletId(transactionDetails.getWallet().getWalletId()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public WalletData getUserWallet(String walletId) {
        var wallet = walletRepository.findByWalletId(walletId);

        if (wallet != null) {
            return wallet;
        }

        wallet = walletRepository.findByUserId(walletId);

        return wallet;
    }

    public List<WalletTransaction> getTransactionsForAUser(String userId) {
        return ledgerRepository.findAllByWallet_WalletId(getUserWallet(userId).getWalletId());
    }

    public WalletData createWallet(String userId) {
        var wallet = new WalletData();
        wallet.setWalletId(uniqueIdGenerator.generateANewId().replace("DEP", "WAL"));
        wallet.setUserId(userId);
        wallet.setRunningBalance(BigInteger.ZERO);
        walletRepository.save(wallet);
        return wallet;
    }
}
