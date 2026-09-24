package com.swasthyaqueue.backend.dto;
import  java.util.List;

public class DashboardResponse {
    private int totalDepartments;
    private int totalPatientsWaiting;
    private List<String> departmentsWithAlerts;

    public DashboardResponse(int totalDepartments, int totalPatientsWaiting, List<String> departmentsWithAlerts) {
        this.totalDepartments = totalDepartments;
        this.totalPatientsWaiting = totalPatientsWaiting;
        this.departmentsWithAlerts = departmentsWithAlerts;
    }

    public int getTotalDepartments() { return totalDepartments; }
    public int getTotalPatientsWaiting() { return totalPatientsWaiting; }
    public List<String> getDepartmentsWithAlerts() { return departmentsWithAlerts; }
}
