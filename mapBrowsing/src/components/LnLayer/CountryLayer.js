﻿import L from "leaflet"
import { LnLayer } from "./LnLayer";

//LnLayer3:乡级勘界线图层
export var CountryLayer=LnLayer.extend({
  initialize(data,options) {
    options.style=function(){
      return{"color": "#be9948","weight": 2.6}
    };
    options.onEachFeature = function (feature, layer) {
      layer.bindTooltip(feature.properties.BOUNDARY_NAME, {
        direction: 'right',
        permanent: false,
        className: 'marker-tooltip-countryBdy'
      });
      layer.on({
        click:((e => {
          var layer = e.target;
          layer._map.clickedID = feature.properties.IDENTIFICATION_CODE;
          layer._map.clickedType = "bdyLayer";
        }))
      });
    };
    L.setOptions(this, options);
    this._layers = {};
    if (data) {
      this.addData(data);
    }
  }
})
