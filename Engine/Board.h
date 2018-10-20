#pragma once
#include "Graphics.h"
class Board
{
private:
	int width;	//480
	int height;	//480
	int cellSize; // 60x60
	int placex;
	int placey;
public:
	class Position
	{
	public:
		char x;
		char y;
		Position() { x = y = '0'; };
		Position(char a, char b) { x = a; y = b; };
		bool operator==(Position & a) { if (x == a.x && y == a.y) return true; return false; };
	};

	Board(Graphics & gfx);
	void gameOver(Graphics & gfx) const;
	void start(Graphics & gfx) const;
	void drawCell(int x, int y, Color c, Graphics & gfx) const;
	void drawCell(int x, int y, int r, int g, int b, Graphics & gfx) const;
	void drawCell(Position z, int r, int g, int b, Graphics & gfx) const;
	void drawBorder(Graphics & gfx) const;
	void drawSegment(int x, int y, Graphics & gfx, int a = 0);
	inline int getWidth() const { return width/cellSize; };
	inline int getHeight() const { return height/cellSize; };
	//inline int getCellSize() const { return cellSize; };
	//inline int getPlacex() const { return placex; };
	//inline int getPlacey() const { return placey; };
	~Board();
};

