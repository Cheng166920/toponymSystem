<template>
	<div></div>
</template>

<script>
import axios from 'axios';
import L from "leaflet"
import "leaflet.marker.highlight/dist/leaflet.marker.highlight.js"
import "leaflet.marker.highlight/dist/leaflet.marker.highlight.css"
	export default {
		methods:{
			searchInfo(clickedID,clickedType){  //图到属性检索
				if(clickedID === undefined) return;
				switch(clickedType){
					case "tpnLayer":  //地名类接口：地名点、地名线
						this.url = "/toponym/toponym/info/code?code=" + clickedID;
						break;
					case "dpLayer":  //门牌类接口：门牌点
						this.url = "/toponym/doorplate/findById?uid=" + clickedID;
						break;
					case "bdyLayer":  //勘界类接口：三交点、界桩点、边界点、各级界线
						this.url = "/toponym/boundary/detail?code=" + clickedID;
						break;
				}
				
				axios.get(this.url,{
					}).then((res) => {
						console.log(res.data.result);
						this.info = res.data.result;
						window.postMessage(res.data.result,'*');  //发生获取到的详细信息
				}).catch((error)=>{
					console.log(error);
				})
			},

			searchMarker(id,type,layer,map){  //属性到图检索
				if(type === "tpnPtLayer" || type === "dpLayer" || type === "tripleLayer" || type === "stakeLayer" || type === "bdyPtLayer" || type === "signLayer"){
					const ptInterface = new Map([["tpnPtLayer","tpnLayer"],["dpLayer","dpLayer"],["tripleLayer","bdyLayer"],["stakeLayer","bdyLayer"],["bdyPtLayer","bdyLayer"],["signLayer","tpnLayer"]]);
					this.searchInfo(id,ptInterface.get(type));
					this.addHighlightPt(map);
				}else{
					const lnInterface = new Map([["tpnLnLayer","tpnLayer"],["cityLayer","bdyLayer"],["countyLayer","bdyLayer"],["countryLayer","bdyLayer"],["vilLayer","bdyLayer"]]);
					this.searchInfo(id,lnInterface.get(type));
					this.addHightlightLn(id,layer,map);
				}
			},

			addHighlightPt(map){  //添加点高亮
				setTimeout(() => {
					let center;
					if(this.info.GEOM) center = JSON.parse(this.info.GEOM).coordinates;
					else center = JSON.parse(this.info[0].geom).coordinates;  //门牌点结构不同
					map.setView([center[1],center[0]], 18);
					setTimeout(() => {
						this.marker = L.marker([center[1],center[0]], {opacity:0, highlight: 'permanent'}).addTo(map);
						setTimeout(() => {  //这里为了测试效果方便，设置了10s后结束高亮，集成后可注释掉55-57行
							this.removeHighlightPt(map);
						}, 10000);
					}, 1000);
				}, 100);
			},

			addHightlightLn(id,layer,map){  //添加线高亮
				setTimeout(() => {
					let center = JSON.parse(this.info.GEOM).coordinates[0][0];
					map.setView([center[1],center[0]], 18);
					setTimeout(() => {
						for(let i in layer._layers){
							if(layer._layers[i].feature.properties.IDENTIFICATION_CODE === id){
								this.line = layer._layers[i];
								this.lineColor = layer._layers[i].options.color;
								layer._layers[i].setStyle({
									weight: 3,
									color: 'yellow'
								});
								break;
							}
						}
						setTimeout(() => {  //这里为了测试效果方便，设置了10s后结束高亮，集成后可注释掉78-80行
							this.removeHighlightLn();
						}, 10000);
					}, 1000);
				}, 100);
			},

			removeHighlightPt(map){  //移除点高亮
				delete this.marker.options.highlight;
				L.DomUtil.removeClass(this.marker._light, "permanent");
				map.removeLayer(this.marker);
			},

			removeHighlightLn(){  //移除线高亮
				this.line.setStyle({
					weight: 1,
					color: this.lineColor
				});
			}
		}
	}
</script>

<style lang="scss" scoped>

</style>