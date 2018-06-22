package com.bonkan.brao.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.bonkan.brao.entity.Entity;
import com.bonkan.brao.entity.entities.TiledObject;

import java.util.ArrayList;

public class Utils {

    /**
     * <p>Parsing {@link com.badlogic.gdx.maps.tiled.TiledMapTileLayer}</p>
     * @param   map
     * @param   layerName
     * @return  ArrayList<<b>Entity</b>>
     */
    public static ArrayList<Entity> splitLayer(TiledMap map, OrthogonalTiledMapRenderer tiled, String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        ArrayList<Entity> rows = new ArrayList<Entity>();

        for(int x = layer.getWidth() - 1; x >= 0; x--){
            for(int y = layer.getHeight()- 1; y >= 0; y--){
                TiledObject e = new TiledObject(x, y, tiled);

                for(Entity r : rows){
                    if(e.getY() == r.getY()){
                        e = (TiledObject) r;
                        break;
                    }
                }
                if (e.getLayer() == null){
                    e.updateLayer(new TiledMapTileLayer(layer.getWidth(),layer.getHeight(), (int)layer.getTileWidth(), (int)layer.getTileHeight()));
                    rows.add(e);
                }

                e.getLayer().setCell(x,y,layer.getCell(x, y));
            }
        }
        return rows;
    }

}
