import L from "leaflet"
import * as turf from '@turf/turf'
import axios from 'axios'
import { collision,grids } from "./Collision"
import "leaflet-canvas-marker"

//点图层
export var PtLayer=L.Class.extend({
  count:0,
  isInitialized:false,
  new_bound:null,
  features:null,

  getPoints: function(layer,map){  //获取当前地图范围的点要素
    if(!this.isInitialized){
      let rgn=null;
      let bound = map.getBounds();
      let res_temp=[]
      var poly = turf.polygon([[
                  [bound._northEast.lng, bound._northEast.lat], 
                  [bound._northEast.lng, bound._southWest.lat],
                  [bound._southWest.lng, bound._southWest.lat],
                  [bound._southWest.lng, bound._northEast.lat],
                  [bound._northEast.lng, bound._northEast.lat]
      ]]);
      for(let i=0;i<poly.geometry.coordinates[0].length;i++){
        res_temp.push(poly.geometry.coordinates[0][i][0]+" "+poly.geometry.coordinates[0][i][1]);
      }
      rgn=res_temp[0]+","+res_temp[1]+","+res_temp[2]+","+res_temp[3]+","+res_temp[4]
      this.getPointsInRgn(rgn,layer,map)
      this.isInitialized=true;
    }
  },

  getPointsInRgn:function(rgn,layer,map){  //获取申请范围内的点要素
    if(this.isScopeRequest){  //是否按范围请求(数据量大的图层一般按范围请求)
      if(rgn === null){  //zoomIn
        this.cleanInvalid(layer,map);
        return;
      }
      layer.isZoomOut = rgn.indexOf("(")!=-1?true:false;
      this.url = this.queryHeader.indexOf("d")!=-1?this.queryHeader + "((" + rgn + "))" : this.queryHeader + "((" + rgn + "))&scale=" + map._zoom;
    }else{
      if(this.isShow === true){return;}  //不按范围请求的数据，请求一次即可
      this.url = this.queryHeader;
      this.isShow = true;
    }
    axios.get(this.url,{
    }).then((res) => {
      if(res.data.result.features === null){  //针对部分区域无数据点
        this.cleanInvalid(layer,map);
        return;
      }
      let markers = this.pointToLayer(res.data.result);
      layer.addMarkers(markers);
      this.cleanInvalid(layer,map);
    })
  },

  cleanInvalid:function(layer,map){  //
    this.new_bound = this.manageLayers(map);
    grids.updateGrids(layer,map);
    collision.updateValidBuf(layer,this.new_bound,map);
  },

  manageLayers:function(map){  //清除已不在显示范围的点要素
    let bound = map.getBounds();
    var new_bound = turf.polygon([[
      [bound._northEast.lng, bound._northEast.lat], 
      [bound._northEast.lng, bound._southWest.lat],
      [bound._southWest.lng, bound._southWest.lat],
      [bound._southWest.lng, bound._northEast.lat],
      [bound._northEast.lng, bound._northEast.lat]
    ]]);
    return new_bound;
  }
  
})
