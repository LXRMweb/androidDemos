# surfaceview的用法

## 相关话题

### 话题1，普通view和SurfaceView的区别

1. 普通view是占用用户界面的，SurfaceView不占用用户界面，它会单独在另一个地方绘制动画
	+ 所以，如果你想要绘制复杂动画，或者需要在绘制过程中经历复杂计算，请使用SurfaceView而非普通view，因为SurfaceView不会阻塞用户界面，但是普通view有可能阻塞用户界面，造成不好的用户使用体验
2. 