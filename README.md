# Bubble
Imitation QQ chat message which can drag remove air bubbles

#Bubble
初始化：
*    BubbleManager.getInstance().init(this);
*		BubbleManager.getInstance().setMaxDragDistance(150);
*		BubbleManager.getInstance().setExplosionTime(150);


使用：调用waterdrop布局
*	  Bubble bubble = (Bubble) convertView.findViewById(R.id.bubble);
	    bubble.setText(String.valueOf(position));
	  	bubble.setOnDragCompeteListener(new OnDragCompeteListener() {

				@Override
				public void onDrag() {
					Toast.makeText(MainActivity.this, "remove:" + position,
							Toast.LENGTH_SHORT).show();
				}
			});
