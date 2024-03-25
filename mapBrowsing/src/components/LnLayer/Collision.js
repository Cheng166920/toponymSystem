import * as turf from '@turf/turf'

export var collision={
    valid_buf:{},//碰撞检测后，得以保留的要素集
    invalid_buf:{},//valid_buf内部比较中淘汰的要素

    //每次地图范围变更后，更新原valid_buf还在新地图窗口的部分
    updateRefValidBuf:function(new_bound,map){
        if(!this.valid_buf) return;
        this.invalid_buf={};
        for(let i in this.valid_buf){
            let length = this.valid_buf[i]._latlngs[0].length;
            var point=turf.point([this.valid_buf[i]._latlngs[0][parseInt(length/2)].lng,this.valid_buf[i]._latlngs[0][parseInt(length/2)].lat]);
            if(!turf.booleanPointInPolygon(point, new_bound)){
              this.invalid_buf[i]=this.valid_buf[i];
              delete this.valid_buf[i];
            }
        }
        for(let i in this.valid_buf){  //valid_buf内部比较是否碰撞
            for(let j in this.valid_buf){
                if(parseInt(i) > parseInt(j) && this.isOverlapped(this.valid_buf[i],this.valid_buf[j],map)){
                    this.invalid_buf[j]=this.valid_buf[j];
                    delete this.valid_buf[j];
                }
            }
        }
    },

    //清空当前要素集
    clearValidBuf:function(){
        this.valid_buf={};
        this.invalid_buf={};
    },

    //使用新数据集，与当前的valid_buf进行判断
    updateValidBuf:function(layer,new_bound,map,isZoomOut){
        if(layer.isClear === true){  //清空图层
            this.clearValidBuf();
            layer.isClear = false;
        }
        if(isZoomOut === true){  //缩小视图后更新valid_buf
            this.updateRefValidBuf(new_bound,map);
        }
        let features = layer._layers;
        for(let i in features){
            if(!features[i]._tooltip) continue;
            if(this.valid_buf.length === 0){  //若valid_buf为空，直接加入
                this.valid_buf[features[i]._leaflet_id] = features[i];
            }
            if(features[i]._leaflet_id in this.invalid_buf){  //清理在invalid_buf里的要素
                if(features[i]._tooltip.options.opacity === 0.9){
                    features[i]._tooltip.options.permanent = false;
                    features[i].getTooltip().setOpacity(0);
                }
            }else if(features[i]._leaflet_id in this.valid_buf){  //valid_buf要素全部显示
                if(features[i]._tooltip.options.opacity === 0){
                    features[i]._tooltip.options.permanent = true;
                    features[i].getTooltip().setOpacity(0.9);
                }
            }else{  //新增要素
                let is_valid = true;  //标记是否为有效要素
                for(let j in this.valid_buf){
                    if(this.isOverlapped(features[i],this.valid_buf[j],map)){
                        is_valid = false;
                        break;
                    }
                }
                if(is_valid){  //若与有效要素均未发生碰撞，则将该要素加入有效要素集
                    features[i]._tooltip.options.permanent = true;
                    if(features[i]._tooltip._container!==undefined){  //tooltip已经打开
                        if(features[i]._tooltip.options.opacity === 0) features[i].getTooltip().setOpacity(0.9);
                    }else{
                        features[i].openTooltip();
                    }
                    this.valid_buf[features[i]._leaflet_id] = features[i];
                }else{
                    features[i]._tooltip.options.permanent = false;
                    if(features[i]._tooltip.options.opacity === 0.9) features[i].getTooltip().setOpacity(0);
                }
            }
        }
    },

    isOverlapped:function(new_elem,ref_elem,map){  //判断要素之间是否碰撞
        var g1=new_elem.grid;  //获取当前element所属的grid
        var g2=ref_elem.grid;
        if(!grids.isGridTouch(g1,g2)){
            return false;
        }

        let new_Width = 90;
        let new_Height = 30;
        let new_length = new_elem._latlngs[0].length;
        let new_containerPoint = map.latLngToContainerPoint(new_elem._latlngs[0][parseInt(new_length/2)]);

        let ref_Width = 90;
        let ref_Height = 30;
        let ref_length = ref_elem._latlngs[0].length;
        let ref_containerPoint = map.latLngToContainerPoint(ref_elem._latlngs[0][parseInt(ref_length/2)]);

        if (Math.abs(new_containerPoint.x - ref_containerPoint.x) < (new_Width/2 + ref_Width/2) &&
        Math.abs(new_containerPoint.y - ref_containerPoint.y) < (new_Height/2 + ref_Height/2)) {
          return true;
        }
        return false;
    }
}

export var grids={
    //创建或更新所有elements所述的grid，需要通过记录每个grid中包含的element引用
    updateGrids:function(features,map){
        for(let i in features){
            if(!features[i]._tooltip) return;
            let length = features[i]._latlngs[0].length;
            let pt_pixel=map.latLngToContainerPoint(features[i]._latlngs[0][parseInt(length/2)]);
            let grid_width = 90;
            let grid_height = 30;
            let col_idx = Math.floor(pt_pixel.x / grid_width);
            let row_idx = Math.floor(pt_pixel.y / grid_height);
            features[i].grid=[col_idx,row_idx];  //将feature所在grid加入属性
        }
    },

    //判断两个grid是否相邻，如果不相邻，基本上其内部要素就不存在碰撞
    isGridTouch:function(grid1,grid2){
        if(Math.abs(grid1[0]-grid2[0])<=1 && Math.abs(grid1[1]-grid2[1])<=1){
            return true;
        }
        return false;
    }
}

