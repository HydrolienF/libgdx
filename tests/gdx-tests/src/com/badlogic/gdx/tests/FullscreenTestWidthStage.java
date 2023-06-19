/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class FullscreenTestWidthStage extends GdxTest {
	SpriteBatch batch;
	Texture tex;
	boolean fullscreen = false;
	BitmapFont font;
	Stage stage;
	Actor actor;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		tex = new Texture(Gdx.files.internal("data/badlogic.jpg"));
		// DisplayMode[] modes = Gdx.graphics.getDisplayModes();
		// for (DisplayMode mode : modes) {
		// System.out.println(mode);
		// }
		Gdx.app.log("FullscreenTest", Gdx.graphics.getBufferFormat().toString());
		updateFullScreen();

		Camera camera = new OrthographicCamera();
		ScreenViewport viewport = new ScreenViewport(camera);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		stage = new Stage(viewport, batch) {
			@Override
			protected boolean isInsideViewport (int screenX, int screenY) {
				boolean b = super.isInsideViewport(screenX, screenY);
				// Gdx.app.log("FullscreenTest","IsInsideViewport(" + screenX + "," + screenY + ") = " + b);
				return b;
			}
		};
		stage.setDebugAll(true);
		actor = new Actor();
		actor.setPosition(0, 0);
		// Only do the expected job with 0, 1
		// actor.setPosition(0, 1);
		actor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(actor);
		inputMultiplexer.addProcessor(stage);

		actor.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log("FullscreenTest", "clicked in " + x + ", " + y);
				// updateFullScreen();
			}
		});
	}

	@Override
	public void resume () {

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		batch.begin();
		batch.setColor(Gdx.input.getX() < Gdx.graphics.getSafeInsetLeft()
			|| Gdx.input.getX() + tex.getWidth() > Gdx.graphics.getWidth() - Gdx.graphics.getSafeInsetRight() ? Color.RED
				: Color.WHITE);
		batch.draw(tex, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		font.draw(batch, "" + Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight(), 0, 20);
		batch.end();
	}

	@Override
	public void resize (int width, int height) {
		// Gdx.app.log("FullscreenTest", "resized: " + width + ", " + height);
		// Gdx.app.log("FullscreenTest", "safe insets: " + Gdx.graphics.getSafeInsetLeft() + "/" +
		// Gdx.graphics.getSafeInsetRight());
		// batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	@Override
	public void pause () {
		Gdx.app.log("FullscreenTest", "paused");
	}

	@Override
	public void dispose () {
		Gdx.app.log("FullscreenTest", "disposed");
	}

	public void updateFullScreen () {
		if (fullscreen) {
			Gdx.graphics.setWindowedMode(1920, 1080);
			// Gdx.graphics.setWindowedMode(480, 320);
			batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
			fullscreen = false;
		} else {
			DisplayMode m = null;
			for (DisplayMode mode : Gdx.graphics.getDisplayModes()) {
				if (m == null) {
					m = mode;
				} else {
					if (m.width < mode.width) {
						m = mode;
					}
				}
			}

			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
			fullscreen = true;
		}
	}
}
