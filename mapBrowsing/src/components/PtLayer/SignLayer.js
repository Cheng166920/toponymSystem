import L from "leaflet"
import {iconLib} from "./IconLib"
import {PtLayer} from './PtLayer'

//PtLayer6：地名标志点图层
export var SignLayer=PtLayer.extend({
  queryHeader:"/toponym/toponym/extent/sign",
  isShow:false,
  isScopeRequest:false,
  pointToLayer:function(data){
    let markers = [];
    let axiosdata=data.features;
    axiosdata.forEach(feature => {
      let {geometry} = feature
      var marker = L.marker([geometry.coordinates[1],geometry.coordinates[0]], { icon: iconLib.getIconObj("sgn") });
      marker.bindTooltip(feature.properties.DIAGRAM_NAME, {
        direction: 'right',
        permanent:true,
        className: 'marker-tooltip-ss',
        tooltipAnchor: [17,0]
      });
      marker.uid = feature.properties.IDENTIFICATION_CODE;
      marker.type = "tpnLayer";
      markers.push(marker);
    })
    return markers;
  }
})