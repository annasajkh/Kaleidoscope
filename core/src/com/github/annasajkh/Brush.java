package com.github.annasajkh;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Brush
{
	Vector2 position;
	float radius;
	
	public Brush(Vector2 position, float radius)
	{
		this.position = position;
		this.radius = radius;
	}
	
	public void draw(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.circle(position.x, position.y, radius);
	}
}
