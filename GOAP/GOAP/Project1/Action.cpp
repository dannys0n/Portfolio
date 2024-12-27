#include "Action.h"

using namespace std;



bool Action::Fufilled(const Action& other) const
{
  bool fufilled = true;

  //find if other action cannot meet any of these needs
  for (const auto& need : m_needs) {
    fufilled = false;
    for (const auto effect : other.m_effects)
    {
      if (need == effect)
      {
        fufilled = true;
      }
    }
    //a need was not met
    if (!fufilled)
    {
      return false;
    }
  }

  return fufilled;
}

bool Action::Fufilled(const WorldState& currentState) const
{
  for (const auto& need : m_needs)
  {
    // Check if the current state satisfies the need
    if (currentState.m_states.find(need) == currentState.m_states.end() || !currentState.m_states.at(need))
    {
      return false;
    }
  }
  return true;
}

void Action::ApplyEffect(WorldState& currentState) const
{
  for (const auto& effect : m_effects)
  {
    // Apply the effect to the current state
    currentState.m_states[effect] = true;
  }
}

WorldState::WorldState()
{
  for (int i = 0; i < State::cStates; i++)
  {
    AddState((State)i);
  }
}

void WorldState::AddAction(Action action)
{
  m_actions.push_back(action);
}

void WorldState::AddState(State state, bool)
{
  m_states.insert(std::make_pair(state, false));
}

void WorldState::UpdateState(State state, bool isTrue)
{
  m_states[state] = isTrue;
}
