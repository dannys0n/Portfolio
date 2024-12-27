#include "ActionPlanner.h"
#include <memory>
#include <queue>
#include <list>
#include <iostream>

//#define WARNING
#define PRINTDEBUG

using namespace std;

enum List
{
  NONE, OPEN, CLOSE
};

//node wrapper for actions
struct Node {

  Node() : parent(nullptr), given(0), heuristic(0), final(0), list(NONE) {};

  std::shared_ptr<Node> parent;
  Action action;
  float given;
  float heuristic;
  float final;
  List list;
  std::vector<State> states;
};

// Create list of actions from nodes
list<Action> ConstructPlan(const std::shared_ptr<Node>& start) {
  list<Action> plan;
  auto current = start;
  while (current) {
    plan.push_back(current->action);
    current = current->parent;
  }
  return plan;
}

// Create list of actions from nodes
void PrintCurrentPlan(const std::shared_ptr<Node>& start) {
  auto current = start;
  cout << "CURRENT PLAN..." << endl;
  while (current) {
    cout << "Action: " << current->action.name << endl;
    current = current->parent;
  }
}

// Pop lowest cost node
std::shared_ptr<Node> PopLowest(vector<std::shared_ptr<Node>>& nodes) {
  auto lowestCostNodeIt = std::min_element(nodes.rbegin(), nodes.rend(), [](const std::shared_ptr<Node>& a, const std::shared_ptr<Node>& b) {
    if (a->final == b->final) {
      return (a->final - a->given) < (b->final - b->given);
    }
    return a->final < b->final;
    });
  std::shared_ptr<Node> lowestCostNode = *lowestCostNodeIt;
  nodes.erase(lowestCostNodeIt.base() - 1);
  return lowestCostNode;
}

//start from goal, make plan backwards
list<Action> ActionPlanner::Plan(State goalState) {
  State desired = goalState;

  // Wrap each action in a node
  size_t size = m_world.m_actions.size();
  vector<std::shared_ptr<Node>> nodes;
  nodes.reserve(size);

  for (size_t i = 0; i < size; ++i) {
    auto node = std::make_shared<Node>();
    node->action = m_world.m_actions[i];
    nodes.push_back(node);
  }

  vector<std::shared_ptr<Node>> openList;

  // Init goal node
  auto goal = std::make_shared<Node>();
  goal->action.m_needs.push_back(desired);
  goal->action.name = "Goal";
  goal->final = 1;
  goal->heuristic = 1;
  goal->list = List::CLOSE;
  openList.push_back(goal);

  // Make plan
  while (!openList.empty()) {
    // Get lowest cost node/action
    std::shared_ptr<Node> curr = PopLowest(openList);

    // Plan is complete
    if (curr->action.Fufilled(m_world)) {
      return ConstructPlan(curr);
    }

#ifdef PRINTDEBUG
    //debug
    PrintCurrentPlan(curr);
#endif // PRINTDEBUG

    // For all actions that fulfill the need
    for (auto& node : nodes) {
      if (curr->action.Fufilled(node->action)) {
        // Neighbor node traversal = action cost
        float givenCost = curr->given + node->action.cost;
        // Action's needs will be heuristic
        float heuristic = curr->action.m_needs.size();
        float finalCost = givenCost + heuristic;

        // If new cost is better or isn't on any list
        if (node->list == List::NONE || finalCost < node->given + node->heuristic) {
          // Update data
          node->parent = curr;
          node->given = givenCost;
          node->heuristic = heuristic;
          node->final = finalCost;

          if (node->list == List::OPEN) {
            continue;
          }

          // Add to open list
          node->list = List::OPEN;
          openList.push_back(node);
        }
      }
    }
  }
#ifdef PRINTDEBUG
  std::string act[] = { "Eat", "Drink", "Sleep", "Heal", "Train", "Craft", "Attack", "Idle", "Running", "Hungry", "Thirsty", "Sleepy", "Damaged", "HoldingApple", "HoldingWeapon", "cStates" };
  int index = (int)goal->action.m_needs.front();
  cout << "Failed Plan Generation for:" << act[index] << endl;
#endif // PRINTDEBUG
  return list<Action>();
}

void ActionPlanner::DoPlan(const std::list<Action>& plan)
{
  float cost = 0;
  std::string act[] = { "Eat", "Drink", "Sleep", "Heal", "Train", "Craft", "Attack", "Idle", "Running", "Hungry", "Thirsty", "Sleepy", "Damaged", "HoldingApple", "HoldingWeapon", "cStates" };
  if (plan.size() == 0)
  {
    cout << "Plan Failed" << endl;
    return;
  }

  cout << "Begin Plan" << endl;

  for (const auto& action : plan)
  {
    cout << "Action:=================" << action.name << endl;
    for (const auto& need : action.m_needs)
    {
      cout << "Prereq: " << act[(int)need] << endl;
    }
    for (const auto& effect : action.m_effects)
    {
      cout << "Effect: " << act[(int)effect] << endl;
    }
    action.ApplyEffect(m_world);
    cost += action.cost;
  }

  cout << "Plan Success" << endl;
  cout << "Total Cost of Plan: " << cost << endl;
}

void ActionPlanner::AddWorldState(WorldState& states)
{
  m_world = states;
}

void ActionPlanner::PrintWorldState()
{
  std::string act[] = { "Eat", "Drink", "Sleep", "Heal", "Train", "Craft", "Attack", "Idle", "Running", "Hungry", "Thirsty", "Sleepy", "Damaged", "HoldingApple", "HoldingWeapon", "cStates" };
  for (const auto& state : m_world.m_states)
  {
    cout << "State: " << state.second << " = " << act[(int)state.first] << endl;
  }
}
