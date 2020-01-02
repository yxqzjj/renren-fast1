package io.renren.modules.generator.dao.impl;

public class WorkPlanDaoImpl {
    private WorkPlanDaoImpl() {}
    private static class SingletonInstance {
        private static final WorkPlanDaoImpl INSTANCE = new WorkPlanDaoImpl();
    }
    public static WorkPlanDaoImpl getWorkPlanDao() {
        return WorkPlanDaoImpl.SingletonInstance.INSTANCE;
    }

}
