#pragma once
#include "Graphics.h"
class Sprite
{
public:
	void drawWPawn(int x, int y, Graphics & gfx);
	void drawBPawn(int x, int y, Graphics & gfx);
	void drawWRook(int x, int y, Graphics & gfx);
	void drawBRook(int x, int y, Graphics & gfx);
	void drawWBishop(int x, int y, Graphics & gfx);
	void drawBBishop(int x, int y, Graphics & gfx);
	void drawWKnight(int x, int y, Graphics & gfx);
	void drawBKnight(int x, int y, Graphics & gfx);
	void drawWKing(int x, int y, Graphics & gfx);
	void drawBKing(int x, int y, Graphics & gfx);
	void drawWQueen(int x, int y, Graphics & gfx);
	void drawBQueen(int x, int y, Graphics & gfx);
	Sprite();
	~Sprite();
};

