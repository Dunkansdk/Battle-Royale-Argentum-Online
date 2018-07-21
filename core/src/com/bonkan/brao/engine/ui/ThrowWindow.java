package com.bonkan.brao.engine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.engine.utils.DigitFilter;

public class ThrowWindow extends Stage {

	private Window potionsWindow;
	
	private final TextField amountText;
	private final TextButton okButton;
	
	private int amount;
	private boolean ready;
	private boolean visible;
	private int potionIndex;
	
	public ThrowWindow()
	{
		super(new ScreenViewport());

		potionsWindow = new Window("Tirar", AssetsManager.getDefaultSkin());
        potionsWindow.setBounds(Gdx.graphics.getWidth() / 2 - 75, Gdx.graphics.getHeight() / 2 - 35, 150, 70);
        
        amountText = new TextField("50", AssetsManager.getDefaultSkin());
        amountText.setAlignment(Align.center);
        amountText.setBounds(potionsWindow.getWidth() / 2 - 40, potionsWindow.getHeight() - 42, 80, 20);
        amountText.setTextFieldFilter(new DigitFilter());
        potionsWindow.addActor(amountText);
        
        okButton = new TextButton("Ok", AssetsManager.getDefaultSkin());
        okButton.setBounds(20, 5, 40, 20);
        
        okButton.addListener(new ClickListener() {
        	public void clicked(InputEvent e, float x, float y)
        	{
        		ready = true;
        		amount = Integer.parseInt(amountText.getText());
        	}
        });
        
        potionsWindow.addActor(okButton);
        
        TextButton cancelButton = new TextButton("Cancelar", AssetsManager.getDefaultSkin());
        cancelButton.setBounds(20 + okButton.getWidth() + 10, 5, 60, 20);
        
        cancelButton.addListener(new ClickListener() {
        	public void clicked(InputEvent e, float x, float y)
        	{
        		ready = true;
        		amount = 0;
        	}
        });
        
        potionsWindow.addActor(cancelButton);
        
        this.addActor(potionsWindow);
        ready = false;
        visible = false;
	}
	
	@Override
	public void draw()
	{
		Color normalColor = this.getBatch().getColor();
		this.getBatch().setColor(normalColor.r, normalColor.g, normalColor.b, 150/255f);
		
		this.getBatch().begin();
			for(Actor a : this.getActors())
				a.draw(this.getBatch(), 150/255f);
		this.getBatch().end();
		
		this.getBatch().setColor(normalColor);
	}
	
	public void reset()
	{
		amountText.setText("50");
		ready = false;
	}

	public void setVisible(boolean val)
	{
		visible = val;
	}
	
	public void setPotionIndex(int index)
	{
		potionIndex = index;
	}
	
	public boolean getVisible()
	{
		return visible;
	}
	
	public boolean getReady()
	{
		return ready;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public int getPotionIndex()
	{
		return potionIndex;
	}
}
