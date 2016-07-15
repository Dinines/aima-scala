package aima.core.environment.vacuum

import aima.core.agent.{Percept, Environment, UnreliableSensor, Agent}
import aima.core.util.DefaultRandomness

/**
  * @author Shawn Garner
  */
class AgentLocationSensor(val agent: Agent) extends UnreliableSensor
  def perceive(environment: Environment): Percept = unreliably() {
    environment.perceive(self)
  }
}

class DirtSensor(val agent: Agent) extends UnreliableSensor
  def perceive(environment: Environment): Percept = unreliably() {
    environment.perceive(self)
  }
}