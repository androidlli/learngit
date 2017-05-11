package com.cango.palmcartreasure.model;

/**
 * Created by cango on 2017/5/10.
 */

public class TaskAbandonRequest {

    /**
     * agencyID : 1
     * caseID : 1
     * applyID : 7555
     * applyCD : BWX55115017548
     */

    private int agencyID;
    private int caseID;
    private int applyID;
    private String applyCD;

    public int getAgencyID() {
        return agencyID;
    }

    public void setAgencyID(int agencyID) {
        this.agencyID = agencyID;
    }

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public int getApplyID() {
        return applyID;
    }

    public void setApplyID(int applyID) {
        this.applyID = applyID;
    }

    public String getApplyCD() {
        return applyCD;
    }

    public void setApplyCD(String applyCD) {
        this.applyCD = applyCD;
    }
}
