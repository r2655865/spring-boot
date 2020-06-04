package com.activiti.config;

import org.activiti.bpmn.model.AssociationDirection;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class HMProcessDiagramCanvas extends DefaultProcessDiagramCanvas {
    protected static Color HIGHLIGHT_COLOR = Color.green;
    protected static Color LABEL_COLOR = Color.BLACK;
    public HMProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType) {
        super(width, height, minX, minY, imageType);
        this.activityFontName = "宋体";
        this.labelFontName = "宋体";
        this.annotationFontName = "宋体";
        this.LABEL_FONT = new Font(this.labelFontName, 2, 10);
        initialize(imageType);
    }

    @Override
    public void drawHighLight(int x, int y, int width, int height) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();
        g.setPaint(HIGHLIGHT_COLOR);
        g.setStroke(THICK_TASK_BORDER_STROKE);
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
        g.draw(rect);
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }
    public void drawCurrentTaskHighLight(int x, int y, int width, int height) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();
        g.setPaint(Color.RED);
        g.setStroke(THICK_TASK_BORDER_STROKE);
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);

        g.draw(rect);
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }
    @Override
    public void drawSequenceflow(int srcX, int srcY, int targetX, int targetY, boolean conditional, boolean highLighted, double scaleFactor) {
        Paint originalPaint = this.g.getPaint();
        if (highLighted) {
            this.g.setPaint(HIGHLIGHT_COLOR);
        }

        java.awt.geom.Line2D.Double line = new java.awt.geom.Line2D.Double((double)srcX, (double)srcY, (double)targetX, (double)targetY);
        this.g.draw(line);
        this.drawArrowHead(line, scaleFactor);
        if (conditional) {
            this.drawConditionalSequenceFlowIndicator(line, scaleFactor);
        }

        if (highLighted) {
            this.g.setPaint(originalPaint);
        }
    }
    @Override
    public void drawSequenceflowWithoutArrow(int srcX, int srcY, int targetX, int targetY, boolean conditional, boolean highLighted, double scaleFactor) {
        Paint originalPaint = this.g.getPaint();
        if (highLighted) {
            this.g.setPaint(HIGHLIGHT_COLOR);
        }

        java.awt.geom.Line2D.Double line = new java.awt.geom.Line2D.Double((double)srcX, (double)srcY, (double)targetX, (double)targetY);
        this.g.draw(line);
        if (conditional) {
            this.drawConditionalSequenceFlowIndicator(line, scaleFactor);
        }

        if (highLighted) {
            this.g.setPaint(originalPaint);
        }
    }
    @Override
    public void drawConnection(int[] xPoints, int[] yPoints, boolean conditional, boolean isDefault, String connectionType, AssociationDirection associationDirection, boolean highLighted, double scaleFactor) {
        Paint originalPaint = this.g.getPaint();
        Stroke originalStroke = this.g.getStroke();
        this.g.setPaint(CONNECTION_COLOR);
        if (connectionType.equals("association")) {
            this.g.setStroke(ASSOCIATION_STROKE);
        } else if (highLighted) {
            this.g.setPaint(HIGHLIGHT_COLOR);
            this.g.setStroke(HIGHLIGHT_FLOW_STROKE);
        }

        for(int i = 1; i < xPoints.length; ++i) {
            Integer sourceX = xPoints[i - 1];
            Integer sourceY = yPoints[i - 1];
            Integer targetX = xPoints[i];
            Integer targetY = yPoints[i];
            java.awt.geom.Line2D.Double line = new java.awt.geom.Line2D.Double((double)sourceX, (double)sourceY, (double)targetX, (double)targetY);
            this.g.draw(line);
        }

        java.awt.geom.Line2D.Double line;
        if (isDefault) {
            line = new java.awt.geom.Line2D.Double((double)xPoints[0], (double)yPoints[0], (double)xPoints[1], (double)yPoints[1]);
            this.drawDefaultSequenceFlowIndicator(line, scaleFactor);
        }

        if (conditional) {
            line = new java.awt.geom.Line2D.Double((double)xPoints[0], (double)yPoints[0], (double)xPoints[1], (double)yPoints[1]);
            this.drawConditionalSequenceFlowIndicator(line, scaleFactor);
        }

        if (associationDirection.equals(AssociationDirection.ONE) || associationDirection.equals(AssociationDirection.BOTH)) {
            line = new java.awt.geom.Line2D.Double((double)xPoints[xPoints.length - 2], (double)yPoints[xPoints.length - 2], (double)xPoints[xPoints.length - 1], (double)yPoints[xPoints.length - 1]);
            this.drawArrowHead(line, scaleFactor);
        }

        if (associationDirection.equals(AssociationDirection.BOTH)) {
            line = new java.awt.geom.Line2D.Double((double)xPoints[1], (double)yPoints[1], (double)xPoints[0], (double)yPoints[0]);
            this.drawArrowHead(line, scaleFactor);
        }

        this.g.setPaint(originalPaint);
        this.g.setStroke(originalStroke);
    }
    public void drawLabel(String text, GraphicInfo graphicInfo, boolean centered) {
        float interline = 1.0F;
        if (text != null && text.length() > 0) {
            Paint originalPaint = this.g.getPaint();
            Font originalFont = this.g.getFont();
            this.g.setPaint(LABEL_COLOR);
            this.g.setFont(LABEL_FONT);
            int wrapWidth = 100;
            int textY = (int)graphicInfo.getY();
            AttributedString as = new AttributedString(text);
            as.addAttribute(TextAttribute.FOREGROUND, this.g.getPaint());
            as.addAttribute(TextAttribute.FONT, this.g.getFont());
            AttributedCharacterIterator aci = as.getIterator();
            FontRenderContext frc = new FontRenderContext((AffineTransform)null, true, false);

            TextLayout tl;
            for(LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc); lbm.getPosition() < text.length(); textY = (int)((float)textY + tl.getDescent() + tl.getLeading() + (interline - 1.0F) * tl.getAscent())) {
                tl = lbm.nextLayout((float)wrapWidth);
                textY = (int)((float)textY + tl.getAscent());
                Rectangle2D bb = tl.getBounds();
                double tX = graphicInfo.getX();
                if (centered) {
                    tX += (double)((int)(graphicInfo.getWidth() / 2.0D - bb.getWidth() / 2.0D));
                }

                tl.draw(this.g, (float)tX, (float)textY);
            }

            this.g.setFont(originalFont);
            this.g.setPaint(originalPaint);
        }

    }
}
