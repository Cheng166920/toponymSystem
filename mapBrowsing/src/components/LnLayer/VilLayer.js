import L from "leaflet"
import { LnLayer } from "./LnLayer";

//LnLayer4:村级勘界线图层
export var VilLayer=LnLayer.extend({
  initialize(data,options) {
    options.style=function(){
      return{"color": "#9BA19A","weight": 1,"fillColor": "#e6ebe5","fillOpacity": 0.1}
    };
    options.onEachFeature = function (feature, layer) {
      // layer.bindTooltip(feature.properties.BOUNDARY_NAME, {
      //   direction: 'right',
      //   permanent: false,
      //   className: 'marker-tooltip-vilBdy',
      //   tooltipAnchor: [0,0]
      // });
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
