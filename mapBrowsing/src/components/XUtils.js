import * as turf from '@turf/turf'
export var xUtils={
    getDifferRgn:function(poly1,bound){
        var differRgn=null;
        let isIntersect = true;
        var poly2 = turf.polygon([[
                      [bound._northEast.lng, bound._northEast.lat], 
                      [bound._northEast.lng, bound._southWest.lat],
                      [bound._southWest.lng, bound._southWest.lat],
                      [bound._southWest.lng, bound._northEast.lat],
                      [bound._northEast.lng, bound._northEast.lat]
        ]]);
        if (turf.booleanContains(poly1, poly2)) {  //原范围包含新范围
            isIntersect = false;
            return differRgn;
              
        }
        if (turf.booleanContains(poly2, poly1)) { //新范围包含原范围
            isIntersect = false;
            let res_temp1=[];
            for(let i=0;i<poly1.geometry.coordinates[0].length;i++){
                res_temp1.push(poly1.geometry.coordinates[0][i][0]+" "+poly1.geometry.coordinates[0][i][1]);
            }
            let res_temp2 = [];
            for (let i = 0; i < poly2.geometry.coordinates[0].length; i++) {
                res_temp2.push(poly2.geometry.coordinates[0][i][0]+" "+poly2.geometry.coordinates[0][i][1]);
            }
            differRgn=res_temp2[0]+", "+res_temp2[3]+", "+res_temp2[2]+", "+res_temp2[1]+", "+res_temp2[4]+"),("+res_temp1[0]+", "+res_temp1[1]+", "+res_temp1[2]+", "+res_temp1[3]+", "+res_temp1[4]
            return differRgn;
        }
        if (isIntersect) {  //原范围新范围相交
            var diff=turf.difference(poly2, poly1);
            let res_temp = [];
            for (let i = 0; i < diff.geometry.coordinates[0].length; i++) {
                res_temp.push(diff.geometry.coordinates[0][i][0]+" "+diff.geometry.coordinates[0][i][1]);
            }
            differRgn=res_temp[0];
            for(let i = 1; i < res_temp.length; i++) {
                differRgn=differRgn + ", " + res_temp[i]
            }
            return differRgn;
        }
        return;
    }
}