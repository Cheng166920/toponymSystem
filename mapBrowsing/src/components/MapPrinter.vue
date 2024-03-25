<template>
	<div>
		<el-form :model="formState" :inline="true" size="mini">
			<el-form-item label="比例尺">
				<el-select v-model="formState.zoom" class="m-2" placeholder="Select" size="mini">
					<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
				</el-select>
			</el-form-item>
			<el-form-item label="中心经度">
				<el-input v-model="formState.lng"></el-input>
			</el-form-item>
			<el-form-item label="中心纬度">
				<el-input v-model="formState.lat"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button @click="customScope">自定义范围</el-button>
				<el-button @click="printMap">打印</el-button>
				<el-button @click="selectAll">全图整饰打印</el-button>
				<el-button @click="selectBox">框选整饰打印</el-button>
			</el-form-item>
		</el-form>
	</div>
  </template>

  <script>
  import "leaflet/dist/leaflet.css";
  require("leaflet.chinatmsproviders/src/leaflet.ChineseTmsProviders.js");
  import domtoimage from 'dom-to-image';
  export default {
	name: "LeafletMap",
	data() {
		return {
			options: [
				{value: "1", label: "1"},
				{value: "2", label: "2"},
				{value: "3", label: "3"},
				{value: "4", label: "4"},
				{value: "5", label: "5"},
				{value: "6", label: "6"},
				{value: "7", label: "7"},
				{value: "8", label: "8"},
				{value: "9", label: "9"},
				{value: "10", label: "10"},
				{value: "11", label: "11"},
				{value: "12", label: "12"},
				{value: "13", label: "13"},
				{value: "14", label: "14"},
				{value: "15",label: "15"},
				{value: "16",label: "16"},
				{value: "17",label: "17"},
				{value: "18",label: "18"},
			],
			formState: {
				zoom: "12",
				lng: "112.20",
				lat: "30.43",
			}
		};
	},
	mounted() {
		this.calcScale();
	},
	methods: {
		printMap(){  //打印
			var node = document.getElementById('map');
			domtoimage
				.toPng(node,{
					width: node.offsetWidth,
					height: node.offsetHeight
				})
				.then(function(dataUrl) {
					var link = document.createElement('a');
					link.download = '打印.jpeg';
					link.href = dataUrl;
					link.click();
				})
				.catch(function(error) {
					console.error("打印失败!", error);
				});
		},

		calcScale(){  //比例尺转换规则
			this.scaleMapping = new Map([[20,"1:600"],[19,"1:1100"],[18,"1:2300"],[17,"1:4500"],[16,"1:9000"],[15,"1:18100"],[14,"1:36100"],[13,"1:72200"],[12,"1:144400"],[11,"1:288900"],[10,"1:577800"]]);
		},

		customScope() {  //自定义范围
			const { zoom, lng, lat } = this.formState;
			this.$emit('customScope', [lat, lng], zoom);
		},

		selectBox(){  //框选整饰打印
			this.$emit('selectBox', this.scaleMapping);
		},

		selectAll(){  //整饰打印
			this.$emit('selectAll', this.scaleMapping);
		}
	}
  };
  </script>
  <style lang="less" scoped>
	.selection-content {
		border: 1px dashed #000;
		background-color: rgba(0, 0, 0, 0.2);
	}
  </style>