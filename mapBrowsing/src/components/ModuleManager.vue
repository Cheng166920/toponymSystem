<template>
	<div></div>
</template>

<script>
import axios from 'axios';
	export default {
		data(){
			return{
				layerOn:{  //默认图层开关
					tpnPtLayer:{name: "地名点图层", on:false},
					dpLayer:{name: "门牌点图层", on:false},
					tripleLayer:{name: "三交点图层", on:false},
					stakeLayer:{name: "界桩点图层", on:false},
					bdyPtLayer:{name: "边界点图层", on:false},
					signLayer:{name: "地名标志点图层", on:false},
					cityLayer:{name: "市级勘界图层", on:false},
					countyLayer:{name: "县级勘界图层", on:false},
					countryLayer:{name: "乡级勘界图层", on:false},
					vilLayer:{name: "村级勘界图层", on:false},
					tpnLnLayer:{name: "地名线图层", on:false}
				}
			}
		},
		mounted(){
			window.addEventListener('message',this.rcvMessage)  //111获取模块消息
		},
		methods:{
			rcvMessage(event){  //接收当前所在模块
				let crtModule = event.data.data;
				this.setLayerOn(crtModule);
			},

			setLayerOn(crtModule){
				//测试数据,集成后删除第36行!!!
				crtModule = "tpnLayer";
				switch(crtModule){
					case "tpnLayer":  //地名模块：地名点、地名线、地名标志
						this.layerOn.tpnPtLayer.on = true;
						this.layerOn.tpnLnLayer.on = true;
						this.layerOn.signLayer.on = true;
						break;
					case "dpLayer":  //门牌模块：门牌点
						this.layerOn.dpLayer.on = true;
						break;
					case "bdyLayer":  //勘界模块：三交点、界桩点、边界点、各级界线
						this.layerOn.cityLayer.on = true;
						this.layerOn.countyLayer.on = true;
						this.layerOn.countryLayer.on = true;
						this.layerOn.vilLayer.on = true;
						break;
				}

			}
		}
	}
</script>

<style lang="scss" scoped>

</style>