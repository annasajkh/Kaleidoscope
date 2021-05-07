package com.github.annasajkh;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Kaleidoscope extends ApplicationAdapter
{
	
	ShapeRenderer shapeRenderer;
	Brush[] brushes;
	float spawnRadius;
	float brushRadius;
	int iterations;
	int brushNum;
	float maxRadius;
	Vector2 posToModify;
	Vector2 velocity;
	float colorHue;
	
	public static Color hsvToRgba(float hue, float saturation, float value, float alpha)
    {

        int h = (int) (hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h)
        {
            case 0:
                return new Color(value, t, p, alpha);
            case 1:
                return new Color(q, value, p, alpha);
            case 2:
                return new Color(p, value, t, alpha);
            case 3:
                return new Color(p, q, value, alpha);
            case 4:
                return new Color(t, p, value, alpha);
            case 5:
                return new Color(value, p, q, alpha);
            default:
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " +
                                           hue +
                                           ", " +
                                           saturation +
                                           ", " +
                                           value);
        }
    }
	

	@Override
	public void create()
	{
		shapeRenderer = new ShapeRenderer();
		brushNum = MathUtils.random(3,MathUtils.random(10,50));
		brushes = new Brush[brushNum];
		brushRadius = MathUtils.random(2,MathUtils.random(10,50));
		spawnRadius = MathUtils.random(250);
		iterations = MathUtils.random(1000,MathUtils.random(5000,10000));
		maxRadius = MathUtils.random(5,10);
		colorHue = MathUtils.random(0,0.999f);
		
		for(float i = 0; i < brushes.length; i++)
		{
			float ratio = (i + 1) / brushes.length;
			
			brushes[(int)i] = new Brush(new Vector2(Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() / 2)
												.add(new Vector2(1,0).rotateDeg(ratio * 360).scl(spawnRadius)),
												brushRadius);
		}
		velocity = new Vector2(1,0).rotateDeg(MathUtils.random(360)).scl(100,300);
	}
	
	public float[] positionToRadius(Vector2 position)
	{
		float[] result = new float[2];
		result[0] = position.angleDeg();
		result[1] = position.cpy().sub(Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() / 2).len();
		return result;
	}
	
	public void update()
	{
		colorHue += MathUtils.random(-0.01f,0.01f);
		colorHue = MathUtils.clamp(colorHue,0,0.999f);
		
		brushes[0].radius += MathUtils.random(-1,1);
		brushes[0].radius = MathUtils.clamp(brushes[0].radius,-maxRadius,maxRadius);
		
		velocity.add(new Vector2(1,0).rotateDeg(MathUtils.random(360)).scl(MathUtils.random(10,20)));
		
		brushes[0].position.x += velocity.x * Gdx.graphics.getDeltaTime();
		brushes[0].position.y += velocity.y * Gdx.graphics.getDeltaTime();
		
		if(brushes[0].position.x < -brushes[0].radius)
		{
			brushes[0].position.x = Gdx.graphics.getWidth() - brushes[0].radius; 
		}
		else if(brushes[0].position.x > Gdx.graphics.getWidth() - brushes[0].radius)
		{
			brushes[0].position.x = -brushes[0].radius;
		}
		
		if(brushes[0].position.y < -brushes[0].radius)
		{
			brushes[0].position.y = Gdx.graphics.getHeight() - brushes[0].radius; 
		}
		else if(brushes[0].position.y > Gdx.graphics.getHeight() - brushes[0].radius)
		{
			brushes[0].position.y = -brushes[0].radius;
		}
		
		posToModify = brushes[0].position;
		
		for(float i = 0; i < brushes.length; i++)
		{
			float ratio = (i + 1) / brushes.length;
			float[] rotationAndRadius = positionToRadius(posToModify);
			
			brushes[(int)i].position = new Vector2(Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() / 2)
											.add(new Vector2(1,0).rotateDeg(ratio * 360)
											.scl(rotationAndRadius[1]).rotateDeg(rotationAndRadius[0]));
			brushes[(int)i].radius = brushes[0].radius;
			
		}
	}

	@Override
	public void render()
	{
		Color color = hsvToRgba(colorHue,1,1,1);
		Gdx.gl.glClearColor(color.r,color.g,color.b,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.begin(ShapeType.Filled);
		for(int i = 0; i < iterations; i++)
		{
			shapeRenderer.setColor(hsvToRgba(colorHue,1,1,1));
			update();
			
			for(Brush brush : brushes)
			{
				brush.draw(shapeRenderer);
			}
		}
		shapeRenderer.end();
		
		create();
		try
		{
			TimeUnit.MILLISECONDS.sleep(500);;
		} 
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void dispose()
	{
		shapeRenderer.dispose();
	}
}
