package com.bonkan.brao.engine.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class TiledMapLayerComponent implements Component {
	
	private TiledMapTileLayer layer;

	public TiledMapLayerComponent(TiledMapTileLayer layer) {
		this.layer = layer;
	}
	
	public TiledMapTileLayer getLayer() {
		return layer;
	}
	
	public TiledMapTileLayer.Cell getCell(int x, int y) {
    	return layer.getCell(x, y);
    }
}
