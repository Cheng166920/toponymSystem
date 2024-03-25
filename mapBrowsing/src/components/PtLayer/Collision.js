import * as turf from '@turf/turf'

export var collision={
    valid_buf:{},//碰撞检测后，得以保留的要素集
    invalid_buf:{},//valid_buf内部比较中淘汰的要素

    //每次地图范围变更后，更新原valid_buf还在新地图窗口的部分
    updateRefValidBuf:function(new_bound,map){
        if(!this.valid_buf) return;
        this.invalid_buf={};
        for(let i in this.valid_buf){
            var point=turf.point([this.valid_buf[i]._latlng.lng,this.valid_buf[i]._latlng.lat]);
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
    updateValidBuf:function(layer,new_bound,map){
        if(layer.isClear === true){
            this.clearValidBuf();
            layer.isClear = false;
        }
        if(layer.isZoomOut === true){
            this.updateRefValidBuf(new_bound,map);
            layer.isZoomOut = false;
        }
        let valid_buf = this.valid_buf;
        let invalid_buf = this.invalid_buf;
        layer._markers && layer._markers.all().forEach(function(d) {
            d.data.valid = false;
            if(valid_buf.length === 0){  //若valid_buf为空，直接加入
                valid_buf[d.data._leaflet_id] = d.data;
            }
            if(!(d.data._leaflet_id in valid_buf) && !(d.data._leaflet_id in invalid_buf)){  //若要素已存在于buf，跳过
                let is_valid = true;  //标记是否为有效要素
                for(let i in valid_buf){
                    if(isOverlapped(d.data,valid_buf[i],map)){
                        is_valid = false;
                        break;
                    }
                }
                if(is_valid){  //若与有效要素均未发生碰撞，则将该要素加入有效要素集
                    valid_buf[d.data._leaflet_id] = d.data;
                }
            }
            
        });
        this.valid_buf = valid_buf;

        function isOverlapped(new_elem,ref_elem,map){  //判断要素之间是否碰撞
            var g1=new_elem.grid;  //获取当前element所属的grid
            var g2=ref_elem.grid;
            if(!grids.isGridTouch(g1,g2)){
                return false;
            }
    
            let new_iconWidth = new_elem.options.icon.options.iconSize[0];
            let new_iconHeight = new_elem.options.icon.options.iconSize[1];
            // let new_tooltipWidth = new_elem.getTooltip()._container.clientWidth;
            // let new_tooltipHeight = new_elem.getTooltip()._container.clientHeight;
            let new_tooltipWidth = 150;
            let new_tooltipHeight = 50;
            let new_containerPoint = map.latLngToContainerPoint(new_elem._latlng);
            let new_transX=(new_elem._tooltip.options.tooltipAnchor[0]+new_iconWidth/2+new_tooltipWidth)/2-new_iconWidth/2;
            let new_transY=new_elem.options.icon.options.iconAnchor[1];
            new_containerPoint.x=new_containerPoint.x + new_transX;
            new_containerPoint.y=new_containerPoint.y + new_transY;
            let new_height=new_iconHeight>new_tooltipHeight?new_iconHeight:new_tooltipHeight;
    
            let ref_iconWidth = ref_elem.options.icon.options.iconSize[0];
            let ref_iconHeight = ref_elem.options.icon.options.iconSize[1];
            // let ref_tooltipWidth = ref_elem.getTooltip()._container.clientWidth;
            // let ref_tooltipHeight = ref_elem.getTooltip()._container.clientHeight;
            let ref_tooltipWidth = 150;
            let ref_tooltipHeight = 50;
            let ref_containerPoint = map.latLngToContainerPoint(ref_elem._latlng);
            let ref_transX=(ref_elem._tooltip.options.tooltipAnchor[0]+ref_iconWidth/2+ref_tooltipWidth)/2-ref_iconWidth/2;
            let ref_transY=ref_elem.options.icon.options.iconAnchor[1];
            ref_containerPoint.x=ref_containerPoint.x + ref_transX;
            ref_containerPoint.y=ref_containerPoint.y + ref_transY;
            let ref_height=ref_iconHeight>ref_tooltipHeight?ref_iconHeight:ref_tooltipHeight;
    
            if (Math.abs(new_containerPoint.x - ref_containerPoint.x) < (new_transX + new_iconWidth/2 + ref_transX + ref_iconWidth/2) &&
            Math.abs(new_containerPoint.y - ref_containerPoint.y) < (new_height + ref_height) / 2) {
              return true;
            }
            return false;
        }
        this.removeOverlap(layer);
    },

    isOverlapped:function(new_elem,ref_elem,map){  //判断要素之间是否碰撞
        var g1=new_elem.grid;  //获取当前element所属的grid
        var g2=ref_elem.grid;
        if(!grids.isGridTouch(g1,g2)){
            return false;
        }

        let new_iconWidth = new_elem.options.icon.options.iconSize[0];
        let new_iconHeight = new_elem.options.icon.options.iconSize[1];
        // let new_tooltipWidth = new_elem.getTooltip()._container.clientWidth;
        // let new_tooltipHeight = new_elem.getTooltip()._container.clientHeight;
        let new_tooltipWidth = 150;
        let new_tooltipHeight = 50;
        let new_containerPoint = map.latLngToContainerPoint(new_elem._latlng);
        let new_transX=(new_elem._tooltip.options.tooltipAnchor[0]+new_iconWidth/2+new_tooltipWidth)/2-new_iconWidth/2;
        let new_transY=new_elem.options.icon.options.iconAnchor[1];
        new_containerPoint.x=new_containerPoint.x + new_transX;
        new_containerPoint.y=new_containerPoint.y + new_transY;
        let new_height=new_iconHeight>new_tooltipHeight?new_iconHeight:new_tooltipHeight;

        let ref_iconWidth = ref_elem.options.icon.options.iconSize[0];
        let ref_iconHeight = ref_elem.options.icon.options.iconSize[1];
        // let ref_tooltipWidth = ref_elem.getTooltip()._container.clientWidth;
        // let ref_tooltipHeight = ref_elem.getTooltip()._container.clientHeight;
        let ref_tooltipWidth = 150;
        let ref_tooltipHeight = 50;
        let ref_containerPoint = map.latLngToContainerPoint(ref_elem._latlng);
        let ref_transX=(ref_elem._tooltip.options.tooltipAnchor[0]+ref_iconWidth/2+ref_tooltipWidth)/2-ref_iconWidth/2;
        let ref_transY=ref_elem.options.icon.options.iconAnchor[1];
        ref_containerPoint.x=ref_containerPoint.x + ref_transX;
        ref_containerPoint.y=ref_containerPoint.y + ref_transY;
        let ref_height=ref_iconHeight>ref_tooltipHeight?ref_iconHeight:ref_tooltipHeight;

        if (Math.abs(new_containerPoint.x - ref_containerPoint.x) < (new_transX + new_iconWidth/2 + ref_transX + ref_iconWidth/2) &&
        Math.abs(new_containerPoint.y - ref_containerPoint.y) < (new_height + ref_height) / 2) {
          return true;
        }
        return false;
    },

    removeOverlap(layer){
        for(let i in this.valid_buf){
            layer._markers && layer._markers.all().forEach(function(d) {
                if(d.data._leaflet_id === parseInt(i)){
                    d.data.valid = true;
                }
            })
        }
        layer._redraw(true);
    }
}

export var grids={
    //创建或更新所有elements所述的grid，需要通过记录每个grid中包含的element引用
    updateGrids:function(layer,map){
        layer._markers && layer._markers.all().forEach(function(d) {
            let pt_pixel=map.latLngToContainerPoint(d.data._latlng)
            let grid_width = 150;
            let grid_height = 100;
            let col_idx = Math.floor(pt_pixel.x / grid_width);
            let row_idx = Math.floor(pt_pixel.y / grid_height);
            d.data.grid=[col_idx,row_idx];  //将feature所在grid加入属性
        });
    },

    //判断两个grid是否相邻，如果不相邻，基本上其内部要素就不存在碰撞
    isGridTouch:function(grid1,grid2){
        if(Math.abs(grid1[0]-grid2[0])<=1 && Math.abs(grid1[1]-grid2[1])<=1){
            return true;
        }
        return false;
    }
}

