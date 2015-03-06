/*
 * Copyright 2003-2005 ExcelUtils http://excelutils.sourceforge.net
 * Created on 2005-6-22
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.tenpay.sm.web.excelutils.tags;

import java.util.StringTokenizer;

import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.tenpay.sm.web.excelutils.ExcelParser;
import com.tenpay.sm.web.excelutils.ExcelUtils;

/**
 * <p>
 * <b>AdjcommentTag </b> is a class which parse the #adjcomment tag, and generate a size-adjustable comment
 * </p>
 * 
 * @author carrickma
 * @date 2013/05/23
 */
public class AdjcommentTag implements ITag {

	public static final String KEY_ADJUSTABLE_COMMENT = "#adjcomment";

	public int[] parseTag(Object context, HSSFSheet sheet, HSSFRow curRow,
			HSSFCell curCell) {
		DynaBean ctx = ExcelUtils.getContext();

		String cellstr = curCell.getStringCellValue();
		int commentBegin = cellstr.indexOf(KEY_ADJUSTABLE_COMMENT);	    
		String commentContent = cellstr.substring(
				commentBegin + KEY_ADJUSTABLE_COMMENT.length()).trim();
		if (commentBegin > 0)
			cellstr = cellstr.substring(0, commentBegin);
		else
			cellstr = "";

		short[] commentSize = new short[]{(short)4, (short)2, (short)6, (short)5};
		
		final String anchorKey = "anchor=";
		if(commentContent.startsWith(anchorKey))
		{
			String anchor = commentContent.substring(anchorKey.length());
			if(anchor.indexOf(" ") > 0)
			{
				commentContent = anchor.substring(anchor.indexOf(" ") + 1);
				anchor = anchor.substring(0, anchor.indexOf(" "));
			}
			else {
				commentContent = "";
			}
			
			String[] anchors = anchor.split(",");
			for(int i=0; i<anchors.length && i<commentSize.length; i++)
			{
				commentSize[i] = (short)Integer.parseInt(anchors[i]);
			}
		}
		if (commentContent.length() > 0) {
			HSSFPatriarch patr = (HSSFPatriarch) ctx.get(KEY_ADJUSTABLE_COMMENT);
			if (patr == null) {
				patr = sheet.createDrawingPatriarch();
				ctx.set(KEY_ADJUSTABLE_COMMENT, patr);
			}
			HSSFComment comment = patr.createComment(new HSSFClientAnchor(0, 0,
					0, 0, (short) commentSize[0], commentSize[1], (short) commentSize[2], commentSize[3]));
			ITag tag = ExcelParser.getTagClass(commentContent);
			if (null != tag) {
				curCell.setCellValue(commentContent);
				int[] shift = tag.parseTag(context, sheet, curRow, curCell);
				commentContent = curCell.getStringCellValue();
			} else {
				commentContent = (String) ExcelParser.parseStr(context,
						commentContent);
			}

			comment.setString(new HSSFRichTextString(commentContent));
			curCell.setCellComment(comment);
		}
		curCell.setCellValue(cellstr);
		ExcelParser.parseCell(context, sheet, curRow, curCell);
		return new int[] { 0, 0, 0 };
	}

	public String getTagName() {
		return KEY_ADJUSTABLE_COMMENT;
	}

	public boolean hasEndTag() {
		return false;
	}
}
