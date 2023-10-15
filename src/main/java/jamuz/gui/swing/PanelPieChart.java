/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jamuz.gui.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.PieChartDataSet;
import org.jCharts.nonAxisChart.PieChart2D;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendAreaProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.PieChart2DProperties;
import org.jCharts.properties.PropertyException;

/**
 * An JPanel extension to display chart pies
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelPieChart extends JPanel  {
	private String title="";  //NOI18N
	private String[] labels=null;
	private Paint[] paints=null;
	private double[] data=null;
	
	/**
	 *
	 */
	public PanelPieChart() {
	}

    private static Logger logger=null;  //Can't be static. Why should it be (as netbeans says) ?
	
	/**
	 * Set logger
	 * @param logger
	 */
	public static void setLogger(Logger logger) {
        PanelPieChart.logger = logger;
	}
    
	/**
	 * This method is called whenever the contents needs to be painted
	 * @param g
	 */
	@Override
	public void paint(Graphics g) {
		if(data!=null) {
			draw(g);
		}
	}

	/**
	 * Create the chart pie
	 * @param title
	 * @param slices
	 */
	public void setPie(String title, ArrayList<PieSlice> slices) {
		this.title=title;
		
		int nbElements=slices.size();
		this.labels= new String[nbElements];
		this.paints= new Paint[nbElements];
		this.data= new double[nbElements];
		int i=0;
		for (PieSlice mySlice : slices) {
			if(mySlice.label.isBlank()) {  //NOI18N
				mySlice.label="-";  //NOI18N
			}
			this.labels[i]=mySlice.label;
			this.paints[i]=mySlice.color;
			this.data[i]=mySlice.value;
			i++;		
		}
		this.repaint();
	}
	
	private void draw(Graphics g) {
		try {
            Graphics2D g2D=(Graphics2D) g;
			PieChart2DProperties pieChart2DProperties=new PieChart2DProperties();
			PieChartDataSet pieChartDataSet;
			LegendProperties legendProperties= new LegendProperties();
			legendProperties.setPlacement( LegendAreaProperties.LEFT );
			legendProperties.setNumColumns( 1 );
			
			pieChartDataSet = new PieChartDataSet(this.title, this.data, this.labels, this.paints, pieChart2DProperties );
			PieChart2D pieChart2D=new PieChart2D(pieChartDataSet, legendProperties, new ChartProperties(), 
                    this.getWidth(), this.getHeight() );
			pieChart2D.setGraphics2D(g2D);
			pieChart2D.render();
			
		} catch (PropertyException | ChartDataException ex) {
			PanelPieChart.logger.log(Level.SEVERE, "ChartPieJCharts.draw()", ex);  //NOI18N
		}
	}
	
	/**
	 * A Pie Slice instance
	 */
	public static class PieSlice implements java.lang.Comparable {
		String label;
		double value;
		Color color;

		/**
		 *
		 * @return
		 */
		public double getValue() {
            return value;
        }

		/**
		 *
		 * @param color
		 */
		public void setColor(Color color) {
            this.color = color;
        }

		/**
		 *
		 * @return
		 */
		public Color getColor() {
            return color;
        }

		/**
		* The slice of the pie.
		 * @param label 
		 * @param value The size of the slice.
		* @param color The color desired for the slice.
		*/
		public PieSlice(String label, double value, Color color) {
			this.label = label;
			this.color = color;
			this.value = value;
		}
		
		/**
		 * Overring method for sorting by decreasing value, for stats in chart display
		 * @param o
		 * @return
		 */
		@Override
		public int compareTo(Object o) {

			//ORDER BY value DESC
			if (this.value == ((PieSlice) o).value) {
				return 0;
			} else if ((this.value) < ((PieSlice) o).value) {
				return 1;
			} else {
				return -1;
			}
		}

		/**
		 *
		 * @param obj
		 * @return
		 */
		@Override
        public boolean equals(Object obj) {
            if(this == obj) {
                return true;
            }
            if (obj instanceof PieSlice) {
                return (this.value == ((PieSlice) obj).value);
            }
            return false;
        }

		/**
		 *
		 * @return
		 */
		@Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
            return hash;
        }
	}
}
