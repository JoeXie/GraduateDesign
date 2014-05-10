package mySource;

import java.awt.Color;
import java.awt.Polygon;

import reference.*;

public class GrainEdge
{
	static final int UP = 0, DOWN = 1, UP_OR_DOWN = 2, LEFT = 3, RIGHT = 4,
			LEFT_OR_RIGHT = 5, NA = 6;

	private int maxPoints = 1000; // ��������߽�����
	private int maxDots = 400; // �����������

	public int quantityOfPoints; // �����߽�����
	public int[] numberOfPointsArray = new int[maxPoints]; // �洢ĳ�������߽���������

	public int[] x_points = new int[maxPoints];// �洢�����߽�����
	public int[] y_points = new int[maxPoints];

	public int width, height, x_start, y_start;
	// private int[] cpixels; //����ֵ����
	Color compareColor = null;

	public boolean yes; // ���ж��Ƿ����ѻ�õĿ���ʱ�õ�

	public int[][] x_PointsOfDotsArray = new int[maxDots][maxPoints];// ��maxPoints0�������ı߽������
	public int[][] y_PointsOfDotsArray = new int[maxDots][maxPoints];
	// ÿһ�� xxpoints�洢�����ص����

	private int cursorOfDots = 0; // n��ʾ��������

	Polygon[] polygon = new Polygon[maxDots];// ����Σ������߽磩����

	int[] area = new int[maxDots]; // ����ռ�����ص����

	public int[] x_CenterOfDot = new int[maxDots]; // ����X[j], Y[j]��ŵ�j��������������
	public int[] y_CenterOfDot = new int[maxDots];
	
	Picture picture = null;
	
	/**
	 * ���캯��
	 * 
	 * @param binaryPicture
	 *            ��ֵͼ��
	 */
	public GrainEdge(Picture binaryPicture)
	{
		//������ʼ��
		picture = binaryPicture;
		width = picture.getWidth();
		height = picture.getHeight();
		// cpixels=new int[width*height];
		
		Color color = null;
		Pixel pixel = null;

		// (ԭ����������ʼλ�ã�Ŀ������������ʼλ�ã�������С)
		// System.arraycopy(newbmp.intData, 0, cpixels, 0,
		// width*height);//������ͼ������ֵ���Ƶ�cpixels������

		// Ѱ�ҳ�ʼ��
		outer1: for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				pixel = picture.getPixel(x, y);
				color = pixel.getColor();

				if (color.equals(Color.black)) // ȷ����ɫΪ��ʼ��
				{
					x_start = x;
					y_start = y;
					break outer1;
				}
			}
		}

		autoOutline(x_start, y_start);
		getRestEdge();
		centerOfGrain();
	}

	
	/*************************************************************************************
	 * private int getBytePixel(int x,int y) �� ��������(x, y)������ֵ.
	 **************************************************************************************/
	// private int getBytePixel(int x,int y)
	// {
	// if(x>= 0 && x< width && y>= 0 && y< height)
	// {
	// return cpixels[y*width+x];
	// }
	// else
	// return 0xFFFFFFFF; //����ͼ���С������Ϊ��ɫ
	// }
	
	
	/**
	 * private boolean inside(int x, int y) �� �жϵ㣨x, y��������ֵ�Ƿ����ʼ����ͬ��
	 * sameWithStartPoint(int x, int y)
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean sameWithStartPoint(int x, int y)
	{
		Color color = picture.getPixel(x, y).getColor();
		//return (color == compareColor); 
		return (compareColor.equals(color));// �Ƿ�ΪĿ���(����Ƚ�)
	}

	/**
	 * boolean isLine(int xs,int ys) �� �ж��Ƿ��ڿ����ڡ�
	 * boolean inDot(int xs,int ys) 
	 * @param xs
	 * @param ys
	 * @return
	 */
	boolean inDot(int xs, int ys)// �ھ���(x,y-5),(x+10,y-5),(x+10,y+5),(x,y+5)�ڽ�������
	{
		int r = 5;
		int xmin = xs;
		int xmax = xs + 2 * r;
		int ymin = ys - r;
		int ymax = ys + r;

		if (xmax > width)
			xmax = width - 1;
		if (ymin < 0)
			ymin = 0;
		if (ymax >= height)
			ymax = height - 1;

		int area = 0;
		int insideCount = 0;

		for (int x = xmin; x <= xmax; x++)
			for (int y = ymin; y <= ymax; y++)
			{
				area++;
				if (sameWithStartPoint(x, y))
					insideCount++;
			}

		return ((double) insideCount) / area >= 0.75; // �ķ�֮�����϶��ڿ����ڣ���ͬһ��
	}

	
	/**
	 * startX,startY���ڿ����ϵĵ㣬xpoints,ypoints�����洢�߽�㣬����table[]���������߽�ɨ�跽��
	 * @param startX
	 * @param startY
	 */
	public void autoOutline(int startX, int startY)
	{
		int x = startX;
		int y = startY;
		int direction;
		compareColor = picture.getPixel(startX, startY).getColor(); // ����ʼ���ֵ

		do
		{
			x++;
		} while (sameWithStartPoint(x, y));

		if (inDot(x, y))
		{
			compareColor = picture.getPixel(startX, startY).getColor(); // ����ʼ���ֵ
			direction = UP;
		} else
		{
			if (!sameWithStartPoint(x - 1, y - 1))
				direction = RIGHT;
			else if (sameWithStartPoint(x, y - 1))
				direction = LEFT;
			else
				direction = DOWN;
		}

		traceEdge(x, y, direction);
	}

	/**
	 * void traceEdge(int xstart, int ystart, int startingDirection)�� ���ұ�Ե
	 * @param startX
	 * @param startY
	 * @param startingDirection
	 */
	void traceEdge(int startX, int startY, int startingDirection)
	{
		int[] table =
		{
				NA, // 0000 0
				RIGHT, // 000X 1
				DOWN, // 00X0 2
				RIGHT, // 00XX 3
				UP, // 0X00 4
				UP, // 0X0X 5
				UP_OR_DOWN, // 0XX0 6
				UP, // 0XXX 7
				LEFT, // X000 8
				LEFT_OR_RIGHT, // X00X 9
				DOWN, // X0X0 10
				RIGHT, // X0XX 11
				LEFT, // XX00 12
				LEFT, // XX0X 13
				DOWN, // XXX0 14
				NA, // XXXX 15
		};
		int index;
		int newDirection;
		int x = startX;
		int y = startY;
		int direction = startingDirection;
		int count = 0;

		boolean UL = sameWithStartPoint(x - 1, y - 1); // UpperLeft
		boolean UR = sameWithStartPoint(x, y - 1); // UpperRight
		boolean LL = sameWithStartPoint(x - 1, y); // LowerLeft
		boolean LR = sameWithStartPoint(x, y); // LowerRight

		do
		{
			index = 0;
			if (LR)
				index |= 1; // index= 00000000 | 00000001
			if (LL)
				index |= 2; // index= 00000000 | 00000010
			if (UR)
				index |= 4; // index= 00000000 | 00000100
			if (UL)
				index |= 8; // index= 00000000 | 00001000
			newDirection = table[index]; // ����4��������ȷ������

			// �ж϶ԽǷֲ�������µķ���
			if (newDirection == UP_OR_DOWN)
			{
				if (direction == RIGHT)
					newDirection = UP;
				else
					newDirection = DOWN;
			}
			if (newDirection == LEFT_OR_RIGHT)
			{
				if (direction == UP)
					newDirection = LEFT;
				else
					newDirection = RIGHT;
			}
			if (newDirection != direction)
			{
				x_points[count] = x;
				y_points[count] = y;
				count++;
			}

			// ���߽���������������ʼ��Сʱ������һ��
			if (count == x_points.length)
			{
				maxPoints *= 2;
				
				int[] xtemp = new int[maxPoints];
				int[] ytemp = new int[maxPoints];
				
				// (ԭ����������ʼλ�ã�Ŀ������������ʼλ�ã�������С)
				System.arraycopy(x_points, 0, xtemp, 0, maxPoints / 2);
				System.arraycopy(y_points, 0, ytemp, 0, maxPoints / 2);
				
				x_points = xtemp;
				y_points = ytemp;

				// ͬʱҲ����洢�߽�������x_PointsOfDotsArray, y_PointsOfDotsArray
				// ԭpublic int[][] x_PointsOfDotsArray = new int[maxDots][maxPoints];
				int[][] x_PointsOfDotsArray_timp = new int[maxDots][maxPoints];
				int[][] y_PointsOfDotsArray_timp = new int[maxDots][maxPoints];

				for (int i = 0; i < maxDots; i++)
				{
					System.arraycopy(x_PointsOfDotsArray[i], 0, x_PointsOfDotsArray_timp[i], 0, maxPoints / 2);
					System.arraycopy(y_PointsOfDotsArray[i], 0, y_PointsOfDotsArray_timp[i], 0, maxPoints / 2);
				}

				x_PointsOfDotsArray = x_PointsOfDotsArray_timp;
				y_PointsOfDotsArray = y_PointsOfDotsArray_timp;

			}
			
			

			switch (newDirection)
			{
			case UP:
				y = y - 1;
				LL = UL;
				LR = UR;
				UL = sameWithStartPoint(x - 1, y - 1);
				UR = sameWithStartPoint(x, y - 1);
				break;
			case DOWN:
				y = y + 1;
				UL = LL;
				UR = LR;
				LL = sameWithStartPoint(x - 1, y);
				LR = sameWithStartPoint(x, y);
				break;
			case LEFT:
				x = x - 1;
				UR = UL;
				LR = LL;
				UL = sameWithStartPoint(x - 1, y - 1);
				LL = sameWithStartPoint(x - 1, y);
				break;
			case RIGHT:
				x = x + 1;
				UL = UR;
				LL = LR;
				UR = sameWithStartPoint(x, y - 1);
				LR = sameWithStartPoint(x, y);
				break;
			}

			direction = newDirection;
		} while ((x != startX || y != startY)); // ��û�лص���ʼ���ʱ��ѭ��ִ�С�

		numberOfPointsArray[cursorOfDots] = count; // �������n�ı߽����
		quantityOfPoints = count;
		polygon[cursorOfDots] = new Polygon(x_points, y_points, quantityOfPoints); // �����n�����ı߽�����
		
		// (ԭ����������ʼλ�ã�Ŀ������������ʼλ�ã�������С)
		System.arraycopy(x_points, 0, x_PointsOfDotsArray[cursorOfDots], 0, quantityOfPoints);
		System.arraycopy(y_points, 0, y_PointsOfDotsArray[cursorOfDots], 0, quantityOfPoints);

		// if(area(n) >= ChangeMyFrame.minGrainSize)//�Ƿ�Ϊ�������ж��������������minGrainSize(10)
		if (getArea(cursorOfDots) >= 10)
		{
			cursorOfDots++;
			
			// �����������������ֵ400�������Ϊ800
			if (cursorOfDots == polygon.length)
			{
				int[][] xtemp = new int[maxDots * 2][maxPoints];
				int[][] ytemp = new int[maxDots * 2][maxPoints];
				Polygon[] ttemp = new Polygon[maxDots * 2];
				int[] areatemp = new int[maxDots * 2];
				System.arraycopy(x_PointsOfDotsArray, 0, xtemp, 0, maxDots);
				System.arraycopy(y_PointsOfDotsArray, 0, ytemp, 0, maxDots);
				System.arraycopy(polygon, 0, ttemp, 0, maxDots);
				System.arraycopy(area, 0, areatemp, 0, maxDots);
				
				x_PointsOfDotsArray = xtemp;
				y_PointsOfDotsArray = ytemp;
				polygon = ttemp;
				area = areatemp;
				
				maxDots *= 2;
			}
		}
	}

	
	/**
	 * public int area(int n) �� ����ÿ����������ͳ�ư������ٸ����أ�
	 * public int getArea(int n)
	 * @param n --����n��ʾ��n����
	 * @return
	 */
	public int getArea(int n) // ����n��ʾ��n����
	{
		area[n] = 0;
		for (int i = 0; i < width - 1; i++)
			for (int j = 0; j < height; j++)
			{
				if (polygon[n].contains(i, j))
					area[n]++;
			}
		return area[n];
	}

	/*********************************************************************
	 * public boolean onEdge(int x, int y) ���жϸõ㣨x��y���Ƿ����ڱ߽��ϻ������
	 **********************************************************************/
	public boolean onEdge(int x, int y)
	{
		for (int nn = 0; nn < cursorOfDots; nn++) // �ж��Ƿ��ڱ߽���
		{
			for (int i = 0; i < polygon[nn].npoints; i++)
			{
				if (x == x_PointsOfDotsArray[nn][i]
						&& y == y_PointsOfDotsArray[nn][i])
					return true;
			}

			if (polygon[nn].contains(x, y)) // �ж��Ƿ��ڶ����ϣ����˴��Ƿ�����ظ���������������
				return true;
		}
		return false;
	}

	/****************************************************************************
	 * void getRestEdge() �� �������߽�
	 *****************************************************************************/
	void getRestEdge()
	{
		// int m_b;
		Pixel pixel = null;
		Color color = null;

		// outer2:
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				yes = onEdge(x, y);
				// m_b= cpixels[y*width+x];
				pixel = picture.getPixel(x, y);
				color = pixel.getColor();
				if (color.equals(Color.black)) // Ϊ��ɫ
				{
					x_start = x;
					y_start = y;
					if (!yes) // �Ҳ������ҵ��Ŀ����ϻ���
					{
						autoOutline(x_start, y_start);
					}
				}
			}

			// ChangeMyFrame.myProgressBar.setValue(30+y*70/height);
			// ChangeMyFrame.myProgressBar.setString("���ڴ����У����Ժ" +
			// String.valueOf(30+y*70/height)+" %");
		}
	}

	/************************************************************************
	 * private void centerOfGrain() �� ��ÿ��������ĵ�
	 *************************************************************************/
	private void centerOfGrain()
	{
		int xmax, ymax, xmin, ymin;
		for (int j = 0; j < cursorOfDots; j++)
		{
			xmax = polygon[j].xpoints[0];
			ymax = polygon[j].ypoints[0];
			xmin = polygon[j].xpoints[0];
			ymin = polygon[j].ypoints[0];

			for (int i = 0; i < polygon[j].npoints; i++)
			{
				if (polygon[j].xpoints[i] > xmax)
					xmax = polygon[j].xpoints[i];
				if (polygon[j].ypoints[i] > ymax)
					ymax = polygon[j].ypoints[i];
				if (polygon[j].xpoints[i] < xmin)
					xmin = polygon[j].xpoints[i];
				if (polygon[j].ypoints[i] < ymin)
					ymin = polygon[j].ypoints[i];
			}

			x_CenterOfDot[j] = (xmax + xmin) / 2; // X[j], Y[j]��ŵ�j�������������
			y_CenterOfDot[j] = (ymax + ymin) / 2;
		}
	}

	// ///////////////////////// get ////////////////////////////////////
	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public Polygon[] getPolygon()
	{
		return polygon;
	}


	public int getQuantityOfDots() {
		return cursorOfDots;
	}

	// ///////////////////////// get ////////////////////////////////////

}

