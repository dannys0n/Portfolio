#include <iostream>

#include <list>
#include "ActionPlanner.h"
#include "UnitTest.h"

void predefinedActions(WorldState& world)
{
  Action action;
  action.name = "Attack";
  action.m_needs.push_back(State::Eat);
  action.m_effects.push_back(State::Attack);
  world.AddAction(action);

  action.m_effects.clear();
  action.m_needs.clear();

  action.name = "Eat Small Ration";
  action.cost = 1;
  action.m_needs.push_back(State::Heal);
  action.m_effects.push_back(State::Eat);
  world.AddAction(action);

  action.m_effects.clear();
  action.m_needs.clear();

  action.name = "Eat Big Ration";
  action.cost = 2;
  action.m_effects.push_back(State::Eat);
  world.AddAction(action);

  action.m_effects.clear();
  action.m_needs.clear();

  action.name = "Use Heal Potion";
  action.cost = 1;
  action.m_effects.push_back(State::Heal);
  world.AddAction(action);
}

void GenerateRandomActions(Action& action, WorldState& world)
{
  //Generate and add random actions
  for (int i = 0; i < 15; ++i) {
    action = GenerateRandomAction(1, 1, 1, 1);
    if (action.m_effects.size() == 0 && action.m_needs.size() == 0)
    {
      continue;
    }
    world.AddAction(action);
  }
}

void test()
{
  // Define initial world state
  ActionPlanner planner;
  WorldState world;
  Action action;

  //generate actions
  predefinedActions(world);
  //GenerateRandomActions(action, world);

  //add actions to worldstate
  planner.AddWorldState(world);
  std::list<Action> plan;
  std::list<Action> shortestPlan;

  //go thorugh every possible goal (single desired state)
  for (size_t i = 0; i < State::cStates; i++)
  {
    plan = planner.Plan((State)i);  //current goal


    if (plan.size() > 2)
    {
      break;
    }
    else if (plan.size() == 2)
    {
      shortestPlan = plan;
    }
  }

  if (plan.size() == 0)
  {
    plan = shortestPlan;
  }

  //do the plan
  planner.DoPlan(plan);
  planner.PrintWorldState();

}


#define _CRT_SECURE_NO_WARNINGS
//for mem leaks
#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>
#include <chrono> //for checking runtime of functions

int main() {

  ////////////////////////////////////////
#ifdef _MSC_VER
  _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
  _CrtSetReportMode(_CRT_WARN, _CRTDBG_MODE_FILE);
  _CrtSetReportFile(_CRT_WARN, _CRTDBG_FILE_STDERR);
#endif
  //start timer
  auto start_time = std::chrono::high_resolution_clock::now();
/////////////////////////////////////////////

  test();

  ///////////////////////////////////
  _CrtDumpMemoryLeaks();
  //print elapsed time
  auto end_time = std::chrono::high_resolution_clock::now();
  auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end_time - start_time);
  //Convert to seconds with decimals
  double seconds = static_cast<double>(duration.count()) / 1e6;
  std::cout << "Runtime: " << seconds << " seconds" << std::endl;
  ////////////////////////////////////
  return 0;
}