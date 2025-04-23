package org.configtestorchestrator.examples;

import org.configtestorchestrator.annotations.Config;
import org.testng.annotations.Test;

public class SampleTest {

    @Test(groups = {"smoke", "api"})
    @Config(global = {
            "loan.approval.required=false"
    })
    public void testFastTrackLoanWorkflow() {
        System.out.println("Running testFastTrackLoanWorkflow with fast-track config");
    }

    @Test(groups = {"regression", "api"})
    @Config(global = {
            "fraudCheck.enabled=true",
            "loan.approval.required=true"
    })
    public void testLoanApprovalWorkflow() {
        System.out.println("Running testLoanApprovalWorkflow with strict config");
    }

    @Test(groups = {"regression", "api"})
    @Config(global = {
            "fraudCheck.enabled=true",
            "loan.approval.required=true"
    })
    public void testSecondStrictWorkflowCase() {
        System.out.println("Running testSecondStrictWorkflowCase with same strict config");
    }

    @Test(groups = {"sanity"})
    @Config(global = {
            "feature.logging.enabled=false",
            "security.mode=relaxed"
    })
    public void testFeatureWithRelaxedSecurity() {
        System.out.println("Running testFeatureWithRelaxedSecurity: logging disabled, relaxed security mode");
    }

    @Test(groups = {"smoke", "api"})
    @Config(global = {
            "feature.logging.enabled=true",
            "security.mode=strict"
    })
    public void testFeatureWithStrictSecurity() {
        System.out.println("Running testFeatureWithStrictSecurity: logging enabled, strict security mode");
    }
}