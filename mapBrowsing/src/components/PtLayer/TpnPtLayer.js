import L from "leaflet"
import {iconLib,secondary,n} from "./IconLib"
import {PtLayer} from "./PtLayer"

//PtLayer1：地名点图层
export var TpnPtLayer = PtLayer.extend({
  queryHeader:"/toponym/toponym/extent/point?polygon=POLYGON",
  isScopeRequest:true,
  pointToLayer:function(data){
    let markers = [];
    for(let i in data){  //i = feature.properties.CATEGORY_CODE
      let axiosdata=data[i].features;
      axiosdata.forEach(feature => {
        let {geometry} = feature
        var flag_c='marker-tooltip'+n.get(i);
        var flag_t=secondary.has(i)?[5,3.5]:[17, 0];
        var marker = L.marker([geometry.coordinates[1],geometry.coordinates[0]], { icon: iconLib.getIconObj(i) });
        marker.bindTooltip(feature.properties.DIAGRAM_NAME, {
          direction: 'right',
          permanent:true,
          className: flag_c,
          tooltipAnchor: flag_t
        });
        marker.uid = feature.properties.IDENTIFICATION_CODE;
        marker.type = "tpnLayer";
        markers.push(marker);
      })
    }
    return markers;
  }
})