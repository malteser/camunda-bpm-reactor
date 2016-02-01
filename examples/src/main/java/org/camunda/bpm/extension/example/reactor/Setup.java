package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;

public class Setup {

  public static ProcessEngineConfiguration CONFIGURATION = new StandaloneInMemProcessEngineConfiguration() {
    {
      this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_TRUE;
      this.getProcessEnginePlugins().add(CamundaReactor.plugin());
      this.jobExecutorActivate = false;
    }
  };

  public static ProcessEngine processEngine = CONFIGURATION.buildProcessEngine();

  public static void init() {
    CamundaEventBus eventBus = CamundaReactor.eventBus();
    new TaskCreateListener(eventBus);
    new TaskAssignListener();
    new CaseTaskCreateListener(eventBus);
  }

}
