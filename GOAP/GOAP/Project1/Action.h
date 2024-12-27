#pragma once
#include <unordered_map>
#include <vector>
#include <string>

enum State { Eat, Drink, Sleep, Heal, Train, Craft, Attack, Idle, Running, Hungry, Thirsty, Sleepy, Damaged, HoldingApple, HoldingWeapon, cStates };

class Action;

//Entity's perception of the world + avaliable actions
struct WorldState
{
	WorldState();

	std::unordered_map<State, bool> m_states; //might change to unordered set for unique states
	std::vector<Action> m_actions; //all avaliable actions

	void AddAction(Action action);
	void AddState(State state, bool = false);
	void UpdateState(State state, bool isTrue = true);
};

//ideally has very few to no needs
//all needs must be met by a single action
class Action
{
public:
	Action() : cost(1), name("") {};

	Action(std::vector<State> needs, std::vector<State> effects, float _cost = 1, std::string _name = "") :
		m_needs(needs),
		m_effects(effects),
		cost(_cost),
		name(_name)
	{};

	//other action fufills our needs
	bool Fufilled(const Action& other) const;

	//world state fufills our needs
	bool Fufilled(const WorldState& current_states) const;

	//apply effects to world state
	void ApplyEffect(WorldState& current_states) const;

	std::string name;

	//cost >= 1
	float cost;

	//pre-req needs and its effects
	std::vector<State> m_needs;
	std::vector<State> m_effects;

private:
	//action
};