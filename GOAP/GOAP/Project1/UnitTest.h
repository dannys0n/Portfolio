#pragma once
#include <random>
#include <vector>
#include <string>
#include <ctime>
#include "Action.h"

// Helper function to generate a random action
Action GenerateRandomAction(int min_needs, int max_needs, int min_effects, int max_effects);