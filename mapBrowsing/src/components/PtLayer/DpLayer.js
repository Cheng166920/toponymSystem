import L from "leaflet"
import {iconLib} from "./IconLib"
import {PtLayer} from './PtLayer'

//PtLayer2：门牌点图层
export var DpLayer=PtLayer.extend({
  queryHeader:"/toponym/toponym/extent/doorplate?polygon=POLYGON",
  isScopeRequest:true,
  pointToLayer:function(data){
    let markers = [];
    let axiosdata=data.features;
    axiosdata.forEach(feature => {
      let {geometry} = feature
      var marker = L.marker([geometry.coordinates[1],geometry.coordinates[0]], { icon: iconLib.getIconObj("dp") });
      var address = feature.properties.road.indexOf("n")!=-1 ? feature.properties.village + feature.properties.doorplate : feature.properties.road + feature.properties.doorplate;
      marker.bindTooltip(address, {
        direction: 'right',
        permanent:true,
        className: 'marker-tooltip-dp',
        tooltipAnchor: [17,0]
      });
      marker.uid = feature.properties.uid;
      marker.type = "dpLayer";
      markers.push(marker);
    })
    return markers;
  }
})