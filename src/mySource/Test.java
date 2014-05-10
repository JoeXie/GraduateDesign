package mySource;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import reference.*;

public class Test
{

	public static void main(String[] args)
	{
		String filename = "E:/CloudFiles/Admin/Eclipse workspace/Graduate Design/Cells.jpg";
		//String filename = FileChooser.pickAFile();
		Picture pic1 = new Picture(filename);
		
//		pic1.explore();
		
		pic1.grayimage();
//		pic1.explore();
		
		// get the threshold
		int threshold = pic1.getThreshold();
		System.out.println(threshold);
		
		//binarizeImage
		pic1.binarizeImage(threshold);
		//pic1.explore();
		
		GrainEdge grainEdage = new GrainEdge(pic1);
		Polygon[] polygon = grainEdage.getPolygon();
		
		//创建一张与pic1大小相同的空白图片
		Picture pic2 = new Picture(pic1.getWidth(), pic1.getHeight());
		Graphics2D gra = (Graphics2D) pic2.getGraphics();
		
//		for(int i = 0; i < polygon.length; i++)
//			gra.drawPolygon(polygon[i]);
//		pic2.explore();
		
		gra.setColor(Color.black);
		for(int i =0; i < grainEdage.getQuantityOfDots(); i++)
		{
				gra.drawPolygon(polygon[i]);
		}
		pic2.show();
		
		
		
		
		
		
		
	}

}

