package com.fellowshipofthe.entityClasses;

public class Transfer {
    private String transferID;
    private String sendingLocationID;
    private String destinationLocationID;
    private String transferDate;
    private String status;
    private String description;

    public String getTransferID() {
        return transferID;
    }

    public void setTransferID(String transferID) {
        this.transferID = transferID;
    }

    public String getSendingLocationID() {
        return sendingLocationID;
    }

    public void setSendingLocationID(String sendingLocationID) {
        this.sendingLocationID = sendingLocationID;
    }

    public String getDestinationLocationID() {
        return destinationLocationID;
    }

    public void setDestinationLocationID(String destinationLocationID) {
        this.destinationLocationID = destinationLocationID;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
