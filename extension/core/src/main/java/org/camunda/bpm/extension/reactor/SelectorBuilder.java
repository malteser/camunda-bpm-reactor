package org.camunda.bpm.extension.reactor;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.extension.reactor.CamundaReactor.processDefintionKey;

public class SelectorBuilder {

  public static SelectorBuilder selector() {
    return new SelectorBuilder();
  }

  public static SelectorBuilder selector(final DelegateTask delegateTask) {
    return selector().process(processDefintionKey(delegateTask.getProcessDefinitionId()))
      .element(delegateTask.getTaskDefinitionKey())
      .event(delegateTask.getEventName());
  }

  public static SelectorBuilder selector(final DelegateExecution delegateExecution) {
    String element = ("sequenceFlow".equals(delegateExecution.getBpmnModelElementInstance().getElementType().getTypeName()))
      ? delegateExecution.getCurrentTransitionId()
      : delegateExecution.getCurrentActivityId();
    return selector().process(processDefintionKey(delegateExecution.getProcessDefinitionId()))
      .element(element)
      .event(delegateExecution.getEventName());
  }

  private final Map<String, String> values = new HashMap<String, String>();

  private SelectorBuilder() {
    // noop
  }

  public SelectorBuilder process(String process) {
    values.put("{process}", process);

    return this;
  }

  public SelectorBuilder element(String element) {
    values.put("{element}", element);

    return this;
  }

  public SelectorBuilder event(String event) {
    values.put("{event}", event);

    return this;
  }

  public Selector build() {
    return Selectors.uri(createTopic());
  }

  public String createTopic() {
    String topic = CamundaReactor.CAMUNDA_TOPIC;
    for (Map.Entry<String, String> entry : values.entrySet()) {
      if (entry.getValue() != null && !"".equals(entry.getKey())) {
        topic = topic.replace(entry.getKey(), entry.getValue());
      }
    }
    return topic;
  }

  @Override
  public String toString() {
    return values.toString();
  }
}