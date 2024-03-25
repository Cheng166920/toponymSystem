import L from "leaflet"
import axios from 'axios'
import * as turf from '@turf/turf'
import { collision,grids } from "./Collision"

//线图层:线状地名+勘界
export var LnLayer=L.GeoJSON.extend({
  new_bound:null,
  compLevel:null,
  getBdyAtZoom:function(zoom,map,id){  //根据缩放级别请求勘界
    let isZoomOut =false;
    let isRedrawed = false;
    if(!this.isInitialized){  //初始化
      this.compLevel = this.getCompLevel(zoom);
      this.isInitialized = true;
      isRedrawed = true;
      this.zoom = zoom;
    }else{
      if(zoom < this.zoom) isZoomOut = true;
      let compLevel = this.getCompLevel(zoom);
      if(compLevel === this.compLevel){  //综合等级不变，不重新请求
        if(id === 4) return;
        this.new_bound = this.manageCompLayer(isRedrawed,map);
        grids.updateGrids(this._layers,map);
        collision.updateValidBuf(this,this.new_bound,map,isZoomOut);
        return;
      }else{  //综合等级改变，重新请求
        this.compLevel = compLevel;
        isRedrawed = true;
      }
    }
    var url = "/toponym/toponym/extent/bd_line?level=" + this.compLevel;
    axios.get(url,{
    }).then((res) => {
      switch(id){
        case 1:
          this.addData(res.data.result["市级"]);
          break;
        case 2:
          this.addData(res.data.result["县级"]);
          break;
        case 3:
          this.addData(res.data.result["乡级"]);
          break;
        case 4:
          this.addData(res.data.result["村级"]);
          break;
        default:
          console.log("无效ID!");
      }
      this.addTo(map);
      this.new_bound = this.manageCompLayer(isRedrawed,map);
      grids.updateGrids(this._layers,map);
      collision.updateValidBuf(this,this.new_bound,map,isZoomOut);
    })
  },

  getCompLevel:function(zoom){  //获取要素综合程度等级
    let compLevel = null;
    if(zoom>=0 && zoom<=7){
      compLevel = "0.1km";
    }else if(zoom>=8 && zoom<=15){
      compLevel = "0.02km";
    }else{
      compLevel = "0km";
    }
    return compLevel;
  },

  manageCompLayer:function(isRedrawed,map){  //添加新综合要素后，清空地图中原有的综合要素
    if(isRedrawed === true){
      for(let i in this._layers){
        for(let j in this._layers){
          if(i < j && this._layers[i]){
            if(this._layers[i]._tooltip && this._layers[j]._tooltip && this._layers[i]._tooltip._content === this._layers[j]._tooltip._content){  //注记
              map.removeLayer(this._layers[i]);
              delete this._layers[i];
            }else if(!this._layers[i]._tooltip && !this._layers[j]._tooltip && this._layers[i].feature.properties.BOUNDARY_NAME === this._layers[j].feature.properties.BOUNDARY_NAME){  //界线
              map.removeLayer(this._layers[i]);
              delete this._layers[i];
            }
          }
        }
      }
    }
    let bound = map.getBounds();
    var new_bound = turf.polygon([[
      [bound._northEast.lng, bound._northEast.lat], 
      [bound._northEast.lng, bound._southWest.lat],
      [bound._southWest.lng, bound._southWest.lat],
      [bound._southWest.lng, bound._northEast.lat],
      [bound._northEast.lng, bound._northEast.lat]
    ]]);
    return new_bound;
  },

  getLines: function(layer,map){  //获取当前地图范围的线要素
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
    this.getLinesInRgn(rgn,layer,map)
  },

  getLinesInRgn:function(rgn,layer,map){  //获取申请范围内的线要素
    if(rgn === null){  //zoomIn
      this.cleanInvalid(layer,map,false);
      return;
    }
    let isZoomOut = rgn.indexOf("(")!=-1?true:false;
    if(isZoomOut){
      for(let i in this._layers){
        this._layers[i].closeTooltip();
        delete this._layers[i]._tooltip._container;
      }
    }
    this.url = this.queryHeader + "((" + rgn + "))&scale=" + map._zoom;
    axios.get(this.url,{
    }).then((res) => {
      if(res.data.result.features === null){
        this.cleanInvalid(layer,map,isZoomOut);
        return;
      }
      for(let i in res.data.result){
        this.addData(res.data.result[i].features);
      }
      this.addTo(map);
      this.cleanInvalid(layer,map,isZoomOut);
    })
  },

  cleanInvalid(layer,map,isZoomOut){
    this.new_bound = this.manageLayer(layer._layers,map);
    grids.updateGrids(layer._layers,map);
    collision.updateValidBuf(layer,this.new_bound,map,isZoomOut);
  },

  manageLayer:function(features,map){  //清除已不在显示范围的线要素
    let bound = map.getBounds();
    var new_bound = turf.polygon([[
      [bound._northEast.lng, bound._northEast.lat], 
      [bound._northEast.lng, bound._southWest.lat],
      [bound._southWest.lng, bound._southWest.lat],
      [bound._southWest.lng, bound._northEast.lat],
      [bound._northEast.lng, bound._northEast.lat]
    ]]);
    // for(let i in features){
    //   if(features[i]._tooltip._latlng !== undefined){
    //     var point = turf.point([features[i]._tooltip._latlng.lng,features[i]._tooltip._latlng.lat]);
    //     if(!turf.booleanPointInPolygon(point, new_bound)){}
    //   }
    // }
    return new_bound;
  }

})
