package reference;


import java.awt.*;
import java.awt.geom.*;

/**
 * A class that represents a picture. This class inherits from SimplePicture and
 * allows the student to add functionality to the Picture class.
 * 
 * Copyright Georgia Institute of Technology 2004-2005
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture
{
	// /////////////////// constructors //////////////////////////////////

	/**
	 * Constructor that takes no arguments
	 */
	public Picture()
	{
		/*
		 * not needed but use it to show students the implicit call to super()
		 * child constructors always call a parent constructor
		 */
		super();
	}

	/**
	 * Constructor that takes a file name and creates the picture
	 * 
	 * @param fileName
	 *            the name of the file to create the picture from
	 */
	public Picture(String fileName)
	{
		// let the parent class handle this fileName
		super(fileName);
	}

	/**
	 * Constructor that takes the width and height
	 * 
	 * @param width
	 *            the width of the desired picture
	 * @param height
	 *            the height of the desired picture
	 */
	public Picture(int width, int height)
	{
		// let the parent class handle this width and height
		super(width, height);
	}

	/**
	 * Constructor that takes a picture and creates a copy of that picture
	 */
	public Picture(Picture copyPicture)
	{
		// let the parent class do the copy
		super(copyPicture);
	}

	
	
	///////////////////////////为寻找边界定义的变量/////////////////////////////
//	
//	static final int UP = 0, DOWN = 1, UP_OR_DOWN = 2, LEFT = 3, RIGHT = 4, LEFT_OR_RIGHT = 5, NA = 6;
//
//	private int maxPoints = 1000; // 初设颗粒边界点个数
//	private int maxPoints0 = 400; // 初设颗粒个数
//
//	public int npoints; // 颗粒边界点个数
//	public int[] nnpoints = new int[maxPoints]; // 存储某个颗粒边界点个数数组
//
//	public int[] xpoints = new int[maxPoints];// 存储颗粒边界数组
//	public int[] ypoints = new int[maxPoints];
//	
//	public int Xstart, Ystart;
//	
//	private int[] cpixels; // 像素值数组
//	private int lowerThreshold, upperThreshold;
//	public boolean yes; // 在判断是否在已获得的颗粒时用到
//
//	public int[][] xxpoints = new int[maxPoints0][maxPoints];// 第maxPoints0个颗粒的边界点数组
//	public int[][] yypoints = new int[maxPoints0][maxPoints];
//	// 每一行 xxpoints存储的像素点个数
//
//	public int n = 0; // n表示颗粒个数
//
//	Polygon[] polygon = new Polygon[maxPoints0];// 多边形（颗粒边界）数组
//
//	int[] area = new int[maxPoints0]; // 颗粒占的像素点个数
//
//	// 数组pointCenterX[j], pointCenterY[j]存放第j个颗粒中心坐标
//	public int[] pointCenterX = new int[maxPoints0];
//	public int[] pointCenterY = new int[maxPoints0];
	
	// //////////////////// methods ///////////////////////////////////////

	/**
	 * Method to return a string with information about this picture.
	 * 
	 * @return a string with information about the picture such as fileName,
	 *         height and width.
	 */
	public String toString()
	{
		String output = "Picture, filename " + getFileName() + " height "
				+ getHeight() + " width " + getWidth();
		return output;

	}
	
	
	//////////////////////////////// usr methods ////////////////////////////

	/**
	 * usr methods, gray the image
	 */
	public void grayimage()
	{
		Pixel[] picPixels = this.getPixels();

		for (int i = 0; i < picPixels.length; i++)
		{
			int red = picPixels[i].getRed();
			int green = picPixels[i].getGreen();
			int blue = picPixels[i].getBlue();

			int gray = (int) (0.30 * red + 0.59 * green + 0.11 * blue);

			picPixels[i].setRed(gray);
			picPixels[i].setGreen(gray);
			picPixels[i].setBlue(gray);
		}
	}

	/**
	 * usr methods. negate the image.
	 */
	public void negate()
	{
		Pixel[] pixArray = this.getPixels();
		Pixel pixel = null;
		int rValue, gValue, bValue;

		// loop through all the pixels
		for (int i = 0; i < pixArray.length; i++)
		{
			pixel = pixArray[i];
			rValue = pixel.getRed();
			gValue = pixel.getGreen();
			bValue = pixel.getBlue();

			pixel.setColor(new Color(255 - rValue, 255 - gValue, 255 - bValue));
		}
	}

	/**
	 * usr methods. a simple edge detection method.
	 * 
	 * @param amount
	 */
	public void edgeDetection(double amount)
	{
		Pixel topPixel = null;
		Pixel bottomPixel = null;
		double topAverage = 0.0;
		double bottomAverage = 0.0;
		int endY = this.getHeight() - 1;

		for (int y = 0; y < endY; y++)
		{
			for (int x = 0; x < this.getWidth(); x++)
			{
				topPixel = this.getPixel(x, y);
				bottomPixel = this.getPixel(x, y + 1);

				topAverage = topPixel.getAverage();
				bottomAverage = bottomPixel.getAverage();

				if (Math.abs(topAverage - bottomAverage) < amount)
				{
					topPixel.setColor(Color.WHITE);
				} else
				{
					topPixel.setColor(Color.BLACK);
				}
			}
		}
	}

	/**
	 * usr method: to draw a string on the current picture.
	 * 
	 * @param text
	 * @param x
	 * @param y
	 */
	public void drawString(String text, int x, int y)
	{
		Graphics g = this.getGraphics();

		g.setColor(Color.black);

		g.setFont(new Font("Arial", Font.BOLD, 24));

		g.drawString(text, x, y);

	}

	/**
	 * usr method --method to copy the passe picture to the current picture.
	 */
	public void copy2D(Picture source, int x, int y)
	{
		Graphics g = this.getGraphics();
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(source.getImage(), x, y, null);
	}

	/**
	 * usr method --method to clip the picture to an ellipse.
	 * 
	 * @return
	 */
	public Picture clipToEllipse()
	{
		int width = this.getWidth();
		int height = this.getHeight();
		Picture result = new Picture(width, height);

		Graphics g = result.getGraphics();
		Graphics2D g2 = (Graphics2D) g;

		Ellipse2D.Double ellipse = new Ellipse2D.Double(0, 0, width, height);

		g2.setClip(ellipse);

		g2.drawImage(this.getImage(), 0, 0, width, height, null);

		return result;
	}

	/**
	 * usr method --获取直方图（直方图是得到每个像素的RGB 值）。【获取直方图前须进行灰度化】
	 * @return
	 */
	public int[] getHistogram() 
	{
		int width = this.getWidth();
		int height = this.getHeight();
		int[] histogram = new int[256]; //save the histogram.
		int value;   // gray value
		
		Pixel pixel = null;
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				pixel = this.getPixel(x, y);
				value = pixel.getRed();// red = green = blue = gray.
				histogram[value]++;
			}
		}
		return histogram; // 返回00 到FF 间每个点的数量数组
	}

	
	/**
	 * usr method --获取阈值
	 * @return
	 */
	//public int getAutoThreshold(BMPDecoder bmp)
	public int getThreshold()
	{
		int level;
		int[] histogram = this.getHistogram(); // get the histogram.
		double result, tempSum1, tempSum2, tempSum3, tempSum4;
		histogram[0] = 0;
		histogram[255] = 0;
		int min = 0;
		while ((histogram[min] == 0) && (min < 255))
			min++;
		int max = 255;
		while ((histogram[max] == 0) && (max > 0))
			max--;
		if (min >= max)
		{
			level = 128;
			return level;
		}
		int movingIndex = min;
		do
		{
			tempSum1 = tempSum2 = tempSum3 = tempSum4 = 0.0;
			for (int i = min; i <= movingIndex; i++)
			{
				tempSum1 += i * histogram[i];
				tempSum2 += histogram[i];
			}
			for (int i = (movingIndex + 1); i <= max; i++)
			{
				tempSum3 += i * histogram[i];
				tempSum4 += histogram[i];
			}
			result = (tempSum1 / tempSum2 / 2.0) + (tempSum3 / tempSum4 / 2.0);
			movingIndex++;
		} while ((movingIndex + 1) <= result && movingIndex <= (max - 1));
		level = (int) Math.round(result);
		return level;
	}

	
	/**
	 * usr method --二值处理，在此之前图像必须进行灰度化。
	 */
	//public void binarizeImage(int level, BMPDecoder bmp)
	public void binarizeImage(int threshold)
	{
		int height = this.getHeight();
		int width = this.getWidth();
		int m_binary;
		Pixel pixel = null;
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				pixel = this.getPixel(x, y);//point to pixel(x, y).
				m_binary = pixel.getRed(); //get the gray value.
				
				if (m_binary <= threshold) // 像素值小于LEVEL 取背景色（黑色）
					pixel.setColor(Color.black);
				else
					pixel.setColor(Color.white); // 像素值大于LEVEL 取前景色（白色）
			}
		}
	}
	
	
	
//	
//	/**
//	 * usr method --找到初始点
//	 */
//	public void getStartPoint()
//	{
//		int x = 0, y = 0;
//		Color color = null;
//		Pixel[] pixels = this.getPixels();
//
//		for (int i = 0; i < pixels.length; i++) // 遍历所有像素
//		{
//			color = pixels[i].getColor();
//			if (color == Color.black) // is black?
//			{
//				x = pixels[i].getX();
//				y = pixels[i].getY();
//				break;                   // 找到一个黑点，则跳出
//			}
//		}
//		Xstart = x;
//		Ystart = y;
//	}
//	
//	
//	/**
//	 * usr method --startX,startY是在颗粒上的点，xpoints,ypoints用来存储边界点，数组table[]用来决定边界扫描方向
//	 * @param startX
//	 * @param startY
//	 */
//	public void autoOutline(int startX, int startY)
//	{
//		int x = startX;
//		int y = startY;
//		int direction;
//
//		// lowerThreshold = upperThreshold= getBytePixel(startX, startY);
//		// //赋初始点的值
//		lowerThreshold = upperThreshold = getPixel(x, y).getRed();// 赋初始点的值
//
//		do
//		{
//			x++;
//		} while (inside(x, y));
//
//		if (isLine(x, y))
//		{
//			lowerThreshold = upperThreshold = getPixel(x, y).getRed();
//			direction = UP;
//		} else
//		{
//			if (!inside(x - 1, y - 1))
//				direction = RIGHT;
//			else if (inside(x, y - 1))
//				direction = LEFT;
//			else
//				direction = DOWN;
//		}
//
//		traceEdge(x, y, direction);
//	}
//
//
//	/**
//	 * usr method --void getRestEdge()：获得其余边界
//	 */
//	public void getRestEdge()
//	{
//		int x, y;
//		Color color = null;
//		Pixel[] pixels = this.getPixels();
//
//		for (int i = 0; i < pixels.length; i++) // 遍历所有像素
//		{
//			color = pixels[i].getColor();
//
//			if (color == Color.black)
//			{
//				x = pixels[i].getX();
//				y = pixels[i].getY();
//				yes = onEdge(x, y);
//				Xstart = x;
//				Xstart = y;
//				if (!yes) // 当（x, y）不在已经找过的点中时。
//					autoOutline(Xstart, Ystart);
//			}
//			// ChangeMyFrame.myProgressBar.setValue(30+y*70/height);
//			// ChangeMyFrame.myProgressBar.setString("正在处理中，请稍侯！"+String.valueOf(30+y*70/height)+" %");
//		}
//	}
//
//	/**
//	 * usr method --public boolean onEdge(int x, int y)：判断该点（x，y）是否已在边界上或颗粒内
//	 * @param x
//	 * @param y
//	 * @return
//	 */
//	private boolean onEdge(int x, int y)
//	{
//		for (int nn = 0; nn < n; nn++) // 判断是否在边界上
//		{
//			for (int i = 0; i < polygon[nn].npoints; i++)
//			{
//				if (x == xxpoints[nn][i] && y == yypoints[nn][i])
//					return true;
//			}
//
//			if (polygon[nn].contains(x, y)) // 判断是否在顶点上？？？？？？？此处是否存在重复操作？？？？
//				return true;
//		}
//		return false;
//	}
//
//	/**
//	 * usr method --void traceEdge(int xstart, int ystart, int startingDirection)：查找边缘
//	 * @param xstart
//	 * @param ystart
//	 * @param startingDirection
//	 */
//	private void traceEdge(int xstart, int ystart, int startingDirection)
//	{
//		int[] table =
//		{ NA, // 0000 0
//				RIGHT, // 000X 1
//				DOWN, // 00X0 2
//				RIGHT, // 00XX 3
//				UP, // 0X00 4
//				UP, // 0X0X 5
//				UP_OR_DOWN, // 0XX0 6
//				UP, // 0XXX 7
//				LEFT, // X000 8
//				LEFT_OR_RIGHT, // X00X 9
//				DOWN, // X0X0 10
//				RIGHT, // X0XX 11
//				LEFT, // XX00 12
//				LEFT, // XX0X 13
//				DOWN, // XXX0 14
//				NA, // XXXX 15
//		};
//		int index;
//		int newDirection;
//		int x = xstart;
//		int y = ystart;
//		int direction = startingDirection;
//		int count = 0;
//
//		boolean UL = inside(x - 1, y - 1); // UpperLeft
//		boolean UR = inside(x, y - 1); // UpperRight
//		boolean LL = inside(x - 1, y); // LowerLeft
//		boolean LR = inside(x, y); // LowerRight
//
//		do
//		{
//			index = 0;
//			if (LR)
//				index |= 1; // index= 00000000 | 00000001
//			if (LL)
//				index |= 2; // index= 00000000 | 00000010
//			if (UR)
//				index |= 4; // index= 00000000 | 00000100
//			if (UL)
//				index |= 8; // index= 00000000 | 00001000
//			newDirection = table[index]; // 根据4邻域像素确定方向
//
//			// 判断对角分布的情况下的方向
//			if (newDirection == UP_OR_DOWN)
//			{
//				if (direction == RIGHT)
//					newDirection = UP;
//				else
//					newDirection = DOWN;
//			}
//			if (newDirection == LEFT_OR_RIGHT)
//			{
//				if (direction == UP)
//					newDirection = LEFT;
//				else
//					newDirection = RIGHT;
//			}
//			if (newDirection != direction)
//			{
//				xpoints[count] = x;
//				ypoints[count++] = y;
//			}
//
//			if (count == xpoints.length)// 当边界颗粒数超出初始值，扩大一倍
//			{
//				int[] xtemp = new int[maxPoints * 2];
//				int[] ytemp = new int[maxPoints * 2];
//				System.arraycopy(xpoints, 0, xtemp, 0, maxPoints);
//				System.arraycopy(ypoints, 0, ytemp, 0, maxPoints);
//				// (原数组名，起始位置，目标数组名，起始位置，拷贝大小)
//				xpoints = xtemp;
//				ypoints = ytemp;
//				maxPoints *= 2;
//			}
//
//			switch (newDirection)
//			{
//			case UP:
//				y = y - 1;
//				LL = UL;
//				LR = UR;
//				UL = inside(x - 1, y - 1);
//				UR = inside(x, y - 1);
//				break;
//			case DOWN:
//				y = y + 1;
//				UL = LL;
//				UR = LR;
//				LL = inside(x - 1, y);
//				LR = inside(x, y);
//				break;
//			case LEFT:
//				x = x - 1;
//				UR = UL;
//				LR = LL;
//				UL = inside(x - 1, y - 1);
//				LL = inside(x - 1, y);
//				break;
//			case RIGHT:
//				x = x + 1;
//				UL = UR;
//				LL = LR;
//				UR = inside(x, y - 1);
//				LR = inside(x, y);
//				break;
//			}
//
//			direction = newDirection;
//		} while ((x != xstart || y != ystart)); // || direction!=
//												// startingDirection));
//
//		nnpoints[n] = count; // 保存颗粒n的边界点数
//		npoints = count;
//		polygon[n] = new Polygon(xpoints, ypoints, npoints); // 保存第n颗粒的边界多边形
//		System.arraycopy(xpoints, 0, xxpoints[n], 0, npoints);
//		System.arraycopy(ypoints, 0, yypoints[n], 0, npoints);
//		// (原数组名，起始位置，目标数组名，起始位置，拷贝大小)
//
//		if (area(n) >= 10)// 是否为颗粒的判断条件：面积大于minGrainSize(10)
//		{
//			n++;
//			if (n == polygon.length) // 如果颗粒数超过初设值400，则该设为800
//			{
//				int[][] xtemp = new int[maxPoints0 * 2][maxPoints];
//				int[][] ytemp = new int[maxPoints0 * 2][maxPoints];
//				Polygon[] ttemp = new Polygon[maxPoints0 * 2];
//				int[] areatemp = new int[maxPoints0 * 2];
//				System.arraycopy(xxpoints, 0, xtemp, 0, maxPoints0);
//				System.arraycopy(yypoints, 0, ytemp, 0, maxPoints0);
//				System.arraycopy(polygon, 0, ttemp, 0, maxPoints0);
//				System.arraycopy(area, 0, areatemp, 0, maxPoints0);
//				xxpoints = xtemp;
//				yypoints = ytemp;
//				polygon = ttemp;
//				area = areatemp;
//				maxPoints0 *= 2;
//			}
//		}
//	}
//
//
//	/**
//	 * usr method --public int area(int n)：计算该颗粒的面积（统计包含多少个像素）
//	 * @param n
//	 * @return
//	 */
//	private int area(int n) // 参数n表示第n个点
//	{
//		int height = this.getHeight();
//		int width = this.getWidth();
//		
//		area[n] = 0;
//		for (int i = 0; i < width - 1; i++)
//			for (int j = 0; j < height; j++)
//			{
//				if (polygon[n].contains(i, j))
//					area[n]++;
//			}
//		return area[n];
//	}
//
//
//	/**
//	 * usr method --boolean isLine(int xs,int ys) ：判断是否在颗粒内
//	 * @param xs
//	 * @param ys
//	 * @return
//	 */
//	private boolean isLine(int xs, int ys)// 在矩形(x,y-5),(x+10,y-5),(x+10,y+5),(x,y+5)内进行运算
//	{
//		int height = this.getHeight();
//		int width = this.getWidth();
//		int r = 5;
//		int xmin = xs;
//		int xmax = xs + 2 * r;
//		int ymin = ys - r;
//		int ymax = ys + r;
//
//		if (xmax > width)
//			xmax = width - 1;
//		if (ymin < 0)
//			ymin = 0;
//		if (ymax >= height)
//			ymax = height - 1;
//
//		int area = 0;
//		int insideCount = 0;
//
//		for (int x = xmin; x <= xmax; x++)
//			for (int y = ymin; y <= ymax; y++)
//			{
//				area++;
//				if (inside(x, y))
//					insideCount++;
//			}
//
//		return ((double) insideCount) / area >= 0.75; // 四分之三以上都在颗粒内，则同一行
//	}
//	
//
//	/**
//	 * usr method --private boolean inside(int x, int y) ： 判断点（x, y）的像素值是否与初始点相同
//	 * @param x
//	 * @param y
//	 * @return
//	 */
//	private boolean inside(int x, int y)
//	{
//		int value;
//		value = getPixel(x, y).getRed();
//		return value == lowerThreshold && value == upperThreshold; // 是否与初始点相同
//		// 其中，lowerThreshold=upperThreshold=getPixel(startX,startY).getRed(startX,startY)
//	}
//	
//	
//	/**
//	 * usr method --private void centerOfGrain() ： 获得颗粒的中心点
//	 */
//	public void centerOfGrain()
//	{
//		int xmax, ymax, xmin, ymin;
//		for (int j = 0; j < n; j++)
//		{
//			xmax = polygon[j].xpoints[0];
//			ymax = polygon[j].ypoints[0];
//			xmin = polygon[j].xpoints[0];
//			ymin = polygon[j].ypoints[0];
//
//			for (int i = 0; i < polygon[j].npoints; i++)
//			{
//				if (polygon[j].xpoints[i] > xmax)
//					xmax = polygon[j].xpoints[i];
//				if (polygon[j].ypoints[i] > ymax)
//					ymax = polygon[j].ypoints[i];
//				if (polygon[j].xpoints[i] < xmin)
//					xmin = polygon[j].xpoints[i];
//				if (polygon[j].ypoints[i] < ymin)
//					ymin = polygon[j].ypoints[i];
//			}
//
//			// 数组pointCenterX[j], pointCenterY[j]存放第j个颗粒中心坐标
//			pointCenterX[j] = (xmax + xmin) / 2;
//			pointCenterY[j] = (ymax + ymin) / 2;
//		}
//	}
	
} // end of class Picture, put all new methods before this
