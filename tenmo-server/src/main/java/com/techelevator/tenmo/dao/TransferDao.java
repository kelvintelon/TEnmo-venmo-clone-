package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfersByUserId(int userId);
    List<Transfer> getAllTransfers();
    Transfer getTransferByTransferId(int transferId);
    Transfer sendBucks(Transfer transfer);


}
