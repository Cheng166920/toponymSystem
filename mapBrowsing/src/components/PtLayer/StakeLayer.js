import L from "leaflet"
import {iconLib} from "./IconLib"
import {PtLayer} from './PtLayer'

//PtLayer4：界桩点图层
export var StakeLayer=PtLayer.extend({
  queryHeader:"/toponym/toponym/extent/stake",
  isShow:false,
  isScopeRequest:false,
  pointToLayer:function(data){
    let markers = [];
    let axiosdata=data.features;
    axiosdata.forEach(feature => {
      let {geometry} = feature
      var marker = L.marker([geometry.coordinates[1],geometry.coordinates[0]], { icon: iconLib.getIconObj("bs") });
      marker.bindTooltip("界桩点", {
        direction: 'right',
        permanent:true,
        className: 'marker-tooltip-00',
        tooltipAnchor: [17,0]
      });
      marker.uid = feature.properties.IDENTIFICATION_CODE;
      marker.type = "bdyLayer";
      markers.push(marker);
    })
    return markers;
  }
})