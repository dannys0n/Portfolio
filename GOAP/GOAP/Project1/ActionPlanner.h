#pragma once

#include "Action.h"
#include "unordered_map"

class ActionPlanner
{
public:
	ActionPlanner() {};

	//return plan to fufill goal
	std::list<Action> Plan(State goal);

	//for testing purposes
	//run entire plan, applies effect of each action
	void DoPlan(const std::list<Action>& plan);

	//populate planner
	void AddWorldState(WorldState& states);
	
	void PrintWorldState();
	
private:
	//planner's current states and avaliable actions
	WorldState m_world;
};