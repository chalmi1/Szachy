#include "FrameTimer.h"

using namespace std::chrono;

float FrameTimer::Mark()
{
	steady_clock::time_point prev = last;
	last = std::chrono::steady_clock::now();
	duration<float> frameTime = last - prev;
	return frameTime.count();
}

FrameTimer::FrameTimer()
{
	last = std::chrono::steady_clock::now();
}


FrameTimer::~FrameTimer()
{
}
