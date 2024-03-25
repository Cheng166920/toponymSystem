import L from "leaflet"
import { LnLayer } from "./LnLayer";

//LnLayer5:地名线图层
export var TpnLnLayer=LnLayer.extend({
  queryHeader:'/toponym/toponym/extent/line?polygon=POLYGON',
  initialize(data,options) {
    options.style=function(feature){
      let category = feature.properties.CATEGORY_CODE.substr(0, 2);  //类别代码前两位即feature对应大类
      if(category==="12"){
        return{"color": "#53BDE6","weight": 1};
      }else if(category==="23"){
        return{"color": "#577FC0","weight": 1};
      }else if(category==="24"){
        return{"color": "#61A2D9","weight": 1};
      }else{category==="26"}{
        return{"color": "#F0923D","weight": 1};
      }
    };
    options.onEachFeature = function (feature, layer) {
      let category = feature.properties.CATEGORY_CODE.substr(0, 2);  //类别代码前两位即feature对应大类
      let flag_c = category==="12"||category==="24"?'marker-tooltip-waterAnno':'marker-tooltip-black';
      layer.bindTooltip(feature.properties.DIAGRAM_NAME, {
        direction: 'right',
        permanent: false,
        className: flag_c
      });
      layer.on({
        click:((e => {
          var layer = e.target;
          layer._map.clickedID = feature.properties.IDENTIFICATION_CODE;
          layer._map.clickedType = "tpnLayer";
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
