import { Line } from '@antv/g2plot';






/*
�����������ı��������ݷ��������У���������һ�����������ʱ��ͼ
*/
fetch('./all_vgg2.json')
  .then((res) => res.json())
  .then((resdata) => {
const data = [];
for(let i = 0; i < 16000; i++){
                let j = i %3;
                if(j ==0){
	data.push({
		time:resdata[i].time,
		score:parseInt(resdata[i].score),
		type:resdata[i].type
	});
              }
}	

//����image1����
const linePlot = new Line(document.getElementById('image1'), {
  description: {
    visible: true,
    text: '',
  },
  forceFit: true,
  padding: 'auto',
  data,
  xField: 'time',
  xAxis: {
        visible: true,
        autoHideLabel: true,
      },
  yField: 'score',
  legend: {
    position: 'right-top',
  },
  seriesField: 'type',
        interactions: [
        {
          type: 'slider',
          cfg: {
            start: 0.1,
            end: 0.2,
          },
        },
      ],
  point: {
    visible: false,
  },
  label: {
    visible: false,
    type: 'point',
  },
});
linePlot.render();
  });
