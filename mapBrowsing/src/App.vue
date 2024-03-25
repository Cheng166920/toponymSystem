<template>
	<MapPrinter ref="printer" @customScope="customScope" @selectBox="selectBox" @selectAll="selectAll"/>
	<LeafletMap ref='map' />
</template>

<script>
import LeafletMap from './components/LeafletMap.vue';
import MapPrinter from './components/MapPrinter.vue';
import L from "leaflet"
import domtoimage from 'dom-to-image';
import "@geoman-io/leaflet-geoman-free";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";
export default {
	name:'App',
	components: {
        LeafletMap,
        MapPrinter
    },
	methods:{
		customScope(center, zoom) {
			this.$refs.map.map.setView(center, zoom);
		},
		selectBox(scaleMapping) {
			var geojsonLayer = null;
			var node = document.getElementById('map');
			this.$refs.map.map.pm.enableDraw("Rectangle", {  //绘制矩形
				finishOn: "dblclick",
				allowSelfIntersection: false,
				tooltips: false
			});
			this.$refs.map.map.on('pm:create', e => {
				geojsonLayer = e.layer;
				geojsonLayer.addTo(this.$refs.map.map);
				var northEast =  e.layer.getBounds()._northEast;
				var southWest =  e.layer.getBounds()._southWest;
				var centerPoint = L.latLng(  //框选矩形的中心点
					(northEast.lat + southWest.lat) / 2.0,
					(northEast.lng + southWest.lng) / 2.0
				);
				var northEastPoint = this.$refs.map.map.latLngToContainerPoint(northEast);  //地理坐标转换屏幕坐标
				var southWestPoint = this.$refs.map.map.latLngToContainerPoint(southWest);
				var width = Math.abs(northEastPoint.x - southWestPoint.x);  //计算框选矩形的宽度以及高度像素
				var height = Math.abs(northEastPoint.y - southWestPoint.y);
				var minx =  //计算框选矩形的左上角屏幕坐标
					northEastPoint.x <= southWestPoint.x
						? northEastPoint.x
						: southWestPoint.x;
				var  miny =
					northEastPoint.y <= southWestPoint.y
						? northEastPoint.y
						: southWestPoint.y;
				if(geojsonLayer){
					this.$refs.map.map.removeLayer(geojsonLayer);
					geojsonLayer = null;
				}
				var scaleText = scaleMapping.get(this.$refs.map.map._zoom);  //缩放级别转换比例尺
				setTimeout(() => {
					domtoimage
						.toPng(node)
						.then(function(dataUrl) {
							if (dataUrl.length <= 6) {
								alert("屏幕截图结果为空,建议放大地图,重新操作试试");
								return;
							}
							var img = new Image();  //过渡img图片,为了截取img指定位置的截图需要
							img.src = dataUrl;
							var canvas = document.createElement("canvas"); //创建canvas元素
							canvas.width = width + 40;
							canvas.height = height + 90;
							var ctx = canvas.getContext('2d');
							ctx.fillStyle="#FFFFFF";
							ctx.fillRect(0,0,width + 30,height + 80);  //白板
							ctx.lineWidth="1";
							ctx.rect(14,62,width + 3,height + 3);  //矩形距离画布左上角水平和垂直距离，矩形的宽高
							ctx.stroke();
							ctx.fillStyle="#000000";  //绘制指北针
							ctx.beginPath();
							ctx.moveTo(35,8);
							ctx.lineTo(20,54);
							ctx.lineTo(35,40);
							ctx.lineTo(50,54);
							ctx.closePath();
							ctx.stroke();
							ctx.fill();
							img.onload = function() {
								ctx.drawImage(img, minx, miny, width, height, 16, 64, width, height);
								ctx.font="13pt Arial";
								ctx.fillStyle = "#000000";
								ctx.textBaseline = 'bottom';
								var titleText = "荆州地名地址";
								ctx.fillText(titleText,(width/2-ctx.measureText(titleText).width/2), 28);  //标题的位置
								ctx.fillText(scaleText,(width/2-ctx.measureText(scaleText).width/2), 56)  //比例尺的位置
								dataUrl = canvas.toDataURL(); //转换图片为dataURL
								var link = document.createElement('a');
								link.download = '框选导出.jpeg';
								link.href = dataUrl;
								link.click();
							};
						})
						.catch(function(error) {
							console.error("打印失败!", error);
						});
				}, 100);
			});
		},
		selectAll(scaleMapping){
			var node = document.getElementById('map');
			var width = node.offsetWidth;
			var height = node.offsetHeight;
			var scaleText = scaleMapping.get(this.$refs.map.map._zoom);  //缩放级别转换比例尺
			domtoimage
				.toPng(node)
				.then(function(dataUrl) {
					if (dataUrl.length <= 6) {
						alert("屏幕截图结果为空,建议放大地图,重新操作试试");
						return;
					}
					var img = new Image();  //过渡img图片,为了截取img指定位置的截图需要
					img.src = dataUrl;
					var canvas = document.createElement("canvas"); //创建canvas元素
					canvas.width = width + 40;
					canvas.height = height + 90;
					var ctx = canvas.getContext('2d');
					ctx.fillStyle="#FFFFFF";
					ctx.fillRect(0,0,width + 40,height + 90);  //白板
					ctx.lineWidth="1";
					ctx.rect(18,64,width + 3,height + 3);  //矩形距离画布左上角水平和垂直距离，矩形的宽高
					ctx.stroke();
					ctx.fillStyle="#000000";  //绘制指北针
					ctx.beginPath();
					ctx.moveTo(35,8);
					ctx.lineTo(20,54);
					ctx.lineTo(35,40);
					ctx.lineTo(50,54);
					ctx.closePath();
					ctx.stroke();
					ctx.fill();
					img.onload = function() {
						ctx.drawImage(img, 0, 0, width, height, 20, 66, width, height);
						ctx.font="13pt Arial";
						ctx.fillStyle = "#000000";
						ctx.textBaseline = 'bottom';
						var titleText = "荆州地名地址";
						ctx.fillText(titleText,(width/2-ctx.measureText(titleText).width/2), 28);  //标题的位置
						ctx.fillText(scaleText,(width/2-ctx.measureText(scaleText).width/2), 56)  //比例尺的位置
						dataUrl = canvas.toDataURL(); //转换图片为dataURL
						var link = document.createElement('a');
						link.download = '全图导出.jpeg';
						link.href = dataUrl;
						link.click();
					};
				})
				.catch(function(error) {
					console.error("打印失败!", error);
				});
		}
	}
}
</script>

<style lang="scss" scoped>
	#app{
		font-family: Avenir, Helvetica, Arial, sans-serif;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
		text-align: center;
		color: #2c3e50;
		margin-top: 60px;
	}
</style>
