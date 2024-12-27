#include "UnitTest.h"

bool goalGenerated = false;
bool startGenerated = false;
State startState;

// Helper function to generate a random action
// only one action with 0 needs can exist
// only one action with 0 effects can exist
Action GenerateRandomAction(int min_needs, int max_needs, int min_effects, int max_effects) {
    static std::mt19937 rng(static_cast<unsigned int>(std::time(nullptr))); // Seed random number generator
    static std::uniform_int_distribution<int> stateDist(0, State::cStates - 1); //states range
    static std::uniform_real_distribution<float> costDist(1.0f, 10.0f); //cost range
    static std::uniform_int_distribution<int> countDistN(min_needs, max_needs); // To decide the number of needs
    static std::uniform_int_distribution<int> countDistE(min_effects, max_effects); // To decide the number of effects
    static std::uniform_int_distribution<int> probabilityDist(1, 100); // Probability distribution

    std::vector<std::string> actionNames = 
    { "Eat", "Drink", "Sleep", "Heal", "Train", "Craft", "Attack", "Idle", "Running", "Hungry", "Thirsty", "Sleepy", "Damaged", "HoldingApple", "HoldingWeapon", "cStates" };

    Action action;

    int needsCount = countDistN(rng);
    if (!startGenerated)
    {
      startGenerated = true;
      startState = static_cast<State>(stateDist(rng));
      action.m_effects.push_back(startState);
      action.name = "No_Needs";
      return action;
    }

    if (goalGenerated)
    {
      goalGenerated = true;
      action.name = "No_Effects";

      // Probability check to avoid the Start state
      State goalState;
      do {
        goalState = static_cast<State>(stateDist(rng));
      } while (goalState == startState); // 0% chance to be the same as start state

      action.m_needs.push_back(goalState);
      return action;
    }

    for (int i = 0; i < needsCount; ++i) {
        action.m_needs.push_back(static_cast<State>(stateDist(rng)));
    }

    int effectsCount = countDistE(rng);

    int name = 0;
    for (int i = 0; i < effectsCount; ++i) {
      State effect = static_cast<State>(stateDist(rng));

        if (i == 0)
        {
          name = (int)effect;
        }

        action.m_effects.push_back(effect);
    }

    action.cost = effectsCount;
    action.name = actionNames[name];
    return action;
}
