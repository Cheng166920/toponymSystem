<template>
	<div id="map">
		<div id="on-off" style='position:absolute; right:10px; top:65px; z-index: 999; color:#696969;'>
			<li v-for="(obj,i) in layerOn" :key="i" style="list-style: none">
				<el-checkbox @click="this.changeState(i)" v-model="obj.on">{{obj.name}}</el-checkbox>
			</li>
		</div>
	</div>
	<POIEvents ref='poiEvents' />
	<ModuleManager ref='moduleManager' />
</template>

<script>
import axios from 'axios';
import L from "leaflet"
import "leaflet/dist/leaflet.css"
require('leaflet.chinatmsproviders/src/leaflet.ChineseTmsProviders.js')
import "leaflet-canvas-marker"
import { TpnPtLayer } from "./PtLayer/TpnPtLayer"
import { DpLayer } from "./PtLayer/DpLayer"
import { TripleLayer } from "./PtLayer/TripleLayer"
import { StakeLayer } from "./PtLayer/StakeLayer"
import { BdyPtLayer } from "./PtLayer/BdyPtLayer"
import { SignLayer } from "./PtLayer/SignLayer"
import { xUtils } from "./XUtils"
import { poiEvents } from "./POIEvents"
import * as turf from '@turf/turf'
import { CityLayer } from "./LnLayer/CityLayer"
import { CountyLayer } from "./LnLayer/CountyLayer"
import { CountryLayer } from "./LnLayer/CountryLayer"
import { VilLayer } from "./LnLayer/VilLayer"
import { TpnLnLayer } from "./LnLayer/TpnLnLayer"
import POIEvents from './POIEvents.vue';
import ModuleManager from './ModuleManager.vue'

export default {
	name: 'LeafletMap',
	data() {
		return {
			poly1:[],
			layerOn:this.layerOn
		}
	},
	components:{
		POIEvents,
		ModuleManager
	},
	mounted() {
		this.initMap();
		this.map.on('movestart', this.onMapMoveStart);
		this.map.on('moveend', this.onMapMoveEnd);
		this.map.on('zoomend', this.onMapZoomEnd);
		this.map.on('click',this.clicked);
		window.addEventListener('message',this.rcvMessage);  //111获取查询消息
	},
	methods: {
		initMap() {
			let tdKey = '6a41cff8a50504d7c86b3962cf280b0c';
			let map10 = L.tileLayer.chinaProvider('TianDiTu.Normal.Map', { key: tdKey, maxZoom: 18, minZoom: 2, })
			let map20 = L.tileLayer.chinaProvider('TianDiTu.Satellite.Map', { key: tdKey, maxZoom: 18, minZoom: 2, })
			let map1 = L.layerGroup([map10])
			let map2 = L.layerGroup([map20])
			this.map = L.map("map", {
				center: [30.43, 112.18], 
				zoom: 15, 
				attributionControl: false, 
				zoomControl: false, 
				layers: [map1],
				preferCanvas: true
			});
			var baseMaps = {
				"街道图": map1,
				"卫星图": map2
			};
			L.control.layers(baseMaps).addTo(this.map);
			this.map.createPane('points');
			this.map.getPane('points').style.zIndex = 9999;
			this.map.createPane('lines1');
			this.map.getPane('lines1').style.zIndex = 201;
			this.map.createPane('lines2');
			this.map.getPane('lines2').style.zIndex = 200;

			this.tpnPtLayer = L.canvasIconLayer({ pane: "points" }).addTo(this.map);  //点1:地名点图层
			this.dpLayer = L.canvasIconLayer({ pane: "points" }).addTo(this.map);  //点2:门牌点图层
			this.tripleLayer = L.canvasIconLayer({ pane: "points" }).addTo(this.map);  //点3:三交点图层
			this.stakeLayer = L.canvasIconLayer({ pane: "points" }).addTo(this.map);  //点4:界桩点图层
			this.bdyPtLayer = L.canvasIconLayer({ pane: "points" }).addTo(this.map);  //点5:边界点图层
			this.signLayer = L.canvasIconLayer({ pane: "points" }).addTo(this.map);  //点6:地名标志点图层
			
			this.cityLayer = new CityLayer(null,{ pane: "lines1" });  //线1：市级勘界图层
			this.countyLayer = new CountyLayer(null,{ pane: "lines1" });  //线2：县级勘界图层
			this.countryLayer = new CountryLayer(null,{ pane: "lines1" });  //线3：乡级勘界图层
			this.vilLayer = new VilLayer(null,{ pane: "lines2" });  //线4：村级勘界图层
			this.tpnLnLayer = new TpnLnLayer(null,{ pane: "lines1" });  //线5：地名线图层

			setTimeout(() => {
				this.layerOn = this.$refs.moduleManager.layerOn;
				if(this.layerOn["tpnPtLayer"].on === true) new TpnPtLayer().getPoints(this.tpnPtLayer, this.map);
				if(this.layerOn["dpLayer"].on === true) new DpLayer().getPoints(this.dpLayer, this.map);
				if(this.layerOn["tripleLayer"].on === true) new TripleLayer().getPoints(this.tripleLayer, this.map);
				if(this.layerOn["stakeLayer"].on === true) new StakeLayer().getPoints(this.stakeLayer, this.map);
				if(this.layerOn["bdyPtLayer"].on === true) new BdyPtLayer().getPoints(this.bdyPtLayer, this.map);
				if(this.layerOn["signLayer"].on === true) new SignLayer().getPoints(this.signLayer, this.map);

				if(this.layerOn["cityLayer"].on === true) this.cityLayer.getBdyAtZoom(this.map._zoom,this.map,1);
				if(this.layerOn["countyLayer"].on === true) this.countyLayer.getBdyAtZoom(this.map._zoom,this.map,2);
				if(this.layerOn["countryLayer"].on === true) this.countryLayer.getBdyAtZoom(this.map._zoom,this.map,3);
				if(this.layerOn["vilLayer"].on === true) this.vilLayer.getBdyAtZoom(this.map._zoom,this.map,4);
				if(this.layerOn["tpnLnLayer"].on === true) this.tpnLnLayer.getLines(this.tpnLnLayer,this.map);
			}, 100);
		},

		onMapMoveStart(){
			let bound = this.map.getBounds();
			this.poly1 = turf.polygon([[
				[bound._northEast.lng, bound._northEast.lat], 
				[bound._northEast.lng, bound._southWest.lat],
				[bound._southWest.lng, bound._southWest.lat],
				[bound._southWest.lng, bound._northEast.lat],
				[bound._northEast.lng, bound._northEast.lat]
			]]);
		},

		onMapMoveEnd(poly1){  //移动后，触发按范围请求的图层事件
			var differRgn=xUtils.getDifferRgn(this.poly1,this.map.getBounds());
			if(this.layerOn["tpnPtLayer"].on === true) new TpnPtLayer().getPointsInRgn(differRgn,this.tpnPtLayer,this.map);
			if(this.layerOn["dpLayer"].on === true) new DpLayer().getPointsInRgn(differRgn,this.dpLayer,this.map);
			if(this.layerOn["tripleLayer"].on === true) new TripleLayer().getPointsInRgn(differRgn,this.tripleLayer,this.map);
			if(this.layerOn["stakeLayer"].on === true) new StakeLayer().getPointsInRgn(differRgn,this.stakeLayer,this.map);
			if(this.layerOn["bdyPtLayer"].on === true) new BdyPtLayer().getPointsInRgn(differRgn,this.bdyPtLayer,this.map);
			if(this.layerOn["signLayer"].on === true) new SignLayer().getPointsInRgn(differRgn,this.signLayer,this.map);
			if(this.layerOn["tpnLnLayer"].on === true) this.tpnLnLayer.getLinesInRgn(differRgn,this.tpnLnLayer,this.map);
		},

		onMapZoomEnd(){  //缩放后，触发按缩放级别请求的图层事件
			if(this.layerOn["cityLayer"].on === true) this.cityLayer.getBdyAtZoom(this.map._zoom,this.map,1);
			if(this.layerOn["countyLayer"].on === true) this.countyLayer.getBdyAtZoom(this.map._zoom,this.map,2);
			if(this.layerOn["countryLayer"].on === true) this.countryLayer.getBdyAtZoom(this.map._zoom,this.map,3);
			if(this.layerOn["vilLayer"].on === true) this.vilLayer.getBdyAtZoom(this.map._zoom,this.map,4);
		},

		changeState(i){  //判断哪个图层的状态改变，以及如何改变
			if(i === "tpnPtLayer" || i === "dpLayer" || i === "tripleLayer" || i === "stakeLayer" || i === "bdyPtLayer" || i === "signLayer"){  //发生变化的是点图层
				let layerC = new Map([["tpnPtLayer",new TpnPtLayer()],["dpLayer",new DpLayer()],["tripleLayer",new TripleLayer()],["stakeLayer",new StakeLayer()],["bdyPtLayer",new BdyPtLayer()],["signLayer",new SignLayer()]]);
				let layer = new Map([["tpnPtLayer",this.tpnPtLayer],["dpLayer",this.dpLayer],["tripleLayer",this.tripleLayer],["stakeLayer",this.stakeLayer],["bdyPtLayer",this.bdyPtLayer],["signLayer",this.signLayer]]);
				this.switchPtLayer(i,layerC.get(i),layer.get(i));
			}else{
				let layer = new Map([["cityLayer",this.cityLayer],["countyLayer",this.countyLayer],["countryLayer",this.countryLayer],["vilLayer",this.vilLayer],["tpnLnLayer",this.tpnLnLayer]]);
				this.switchLnLayer(i,layer.get(i));
			}
		},

		switchPtLayer(i,layerC,layer){  //开关点图层
			if(this.layerOn[i].on === false){  //关转开
				layerC.getPoints(layer, this.map);
			}else{  //开转关
				layer._markers && layer._markers.all().forEach(function(d) {
					d.data.closeTooltip();
				});
				layer.clearLayers();
				layer.isClear = true;
			}
		},

		switchLnLayer(i,layer){  //开关线图层
			if(this.layerOn[i].on === false){  //关转开
				layer.getLines(layer,this.map);
			}else{  //开转关
				layer.remove();
				layer.isClear = true;
				layer.isInitialized = false;
				for(let i in layer._layers){
					if(layer._layers[i]._tooltip && layer._layers[i]._tooltip._container!==undefined){
						delete layer._layers[i]._tooltip._container;
					}
				}
				setTimeout(() => {  //如果关闭图层时恰有已向后端发送请求，但还在请求路上的数据，待显示后清空
					layer.remove();
				}, 8000);
			}
		},

		clicked(){  //点击事件
			//this.$refs.poiEvents.searchInfo(this.map.clickedID,this.map.clickedType);
			var url = '/geoserver/geoserver/gwc/service/tms/1.0.0/cite%3Ajn_area@EPSG%3A3857@pbf'
				axios.get(url,{
					}).then((res) => {
						console.log(res.data);
				}).catch((error)=>{
					console.log(error);
				})
		},

		rcvMessage(event){  //接收查询结果A的id和类型
			if(event.data.data === undefined) return;
			let id = event.data.data.id; let type = event.data.data.type;
			// let id = "e4d77b48-886f-401f-b5ce-7339c07fddc5"; let type = "tpnPtLayer";  //点测试数据
			// let id = "b58cade0-85ae-4a58-a0a9-0ffd591a328f";let type = "tpnLnLayer";  //线测试数据
			if(this.id === id) return;
			this.id = id;
			const mapping = new Map([["tpnPtLayer",this.tpnPtLayer],["dpLayer",this.dpLayer],["tripleLayer",this.tripleLayer],["stakeLayer",this.stakeLayer],["bdyPtLayer",this.bdyPtLayer],["cityLayer",this.cityLayer],["countyLayer",this.countyLayer],["countryLayer",this.countryLayer],["vilLayer",this.vilLayer],["tpnLnLayer",this.tpnLnLayer]]);
			if(id === null){  //清除查询结果
				if(type === "tpnPtLayer" || type === "dpLayer" || type === "tripleLayer" || type === "stakeLayer" || type === "bdyPtLayer" || type === "signLayer"){
					this.$refs.poiEvents.removeHighlightPt(this.map);
				}else{
					this.$refs.poiEvents.removeHighlightPt(id,mapping.get(type),this.map);
				}
				return;
			}
			this.$refs.poiEvents.searchMarker(id,type,mapping.get(type),this.map);
		}
	}
}
</script>

<style src="@/components/XAnno.css"  scoped></style>
<style lang="scss" scoped>
	#map {
		width: 100%;
		height: 865px;
		border: 0px solid #42B983;
    }
	.el-checkbox{
		color:#000000;
	}
</style>