/* Copyright (c) 2010-2014 Pierre LEVY androidsoft.org
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
package org.androidsoft.games.memory.kids.ui;

import java.text.MessageFormat;

import org.androidsoft.games.memory.kids.PreferencesService;
import org.androidsoft.games.memory.kids.model.Memory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fairket.app.memory.kids.R;
import com.fairket.sdk.android.FairketApiClient;
import com.fairket.sdk.android.FairketAppTimeHelper;

/**
 * MainActivity
 * 
 * @author pierre
 */
public class MainActivity extends AbstractMainActivity implements
		Memory.OnMemoryListener {

	public static final String FAIRKET_LOG_TAG = "FairketKidsMemory";

	// Integ app key
	//public static final String FAIRKET_APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAssUd0bNodBrziReXb1J8djrsP6KCG2lxnnBZqgC38jrpnhHLsjxIvRE6/DpydwUyFrye58sSNzuxRg5rbemy7SThVL3stqeNMHNYDlicfaqSRfNsrrcSyN2tLYtzRtDpED6eH5WAGjDfFJ2GY+Qu7nST44epO2RaPBAKlcoVFLE+y6PKmxmGyXzkTOnI+9MDAbo6HJQ/EGbjERMF4B9PmYYWMoxfgkP5Zj91uHSLogVl93Pd8VTuhwia3p7RVsfJ1OatXSjo3MErzwq2tdjfzHhcup3EcOUAQy38uCLrpqqJFLRkeiulWVGP4uojMzD7mxeyvW3JLzW8GtClMt6/PwIDAQAB";
	// Prod app key
	public static final String FAIRKET_APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjtsuPFU4uiH+cz1ApG/2B44QG9eF0lo84g7imInDgo1WHESEXMiqkDZBmnVSx2sPz0tr4EeJSLCo9jbvZGIrtY+qCDbi/hmt32pP4JzEqkWn3rIoatQhSLOPGxNFmacjTmuj2pDDWE69bLQFy1nCDnxKk0Lij1hfPaXvWnmveZOcaFgCybQGV6mtI+NV8kOommqAFICXjrNKDKKyQM3GrZU4XWgC8ySmyXMQIfbMbI1JKZpIeleisBTFVlc270S2ZGlMRBByGjiD8OIAhxkVSS1AVoIHiqmTj6pXfOjMMjtf4BZVBQUEPxZWGj588HzwfskSO12kPwgHadUS6KNCJwIDAQAB";

	private static final int[] tiles_default = { R.drawable.default_1,
			R.drawable.default_2, R.drawable.default_3, R.drawable.default_4,
			R.drawable.default_5, R.drawable.default_6, R.drawable.default_7,
			R.drawable.default_8, R.drawable.default_9, R.drawable.default_10,
			R.drawable.default_11, R.drawable.default_12,
			R.drawable.default_13, R.drawable.default_14,
			R.drawable.default_15, R.drawable.default_16,
			R.drawable.default_17, R.drawable.default_18,
			R.drawable.default_19, R.drawable.default_20,
			R.drawable.default_21, R.drawable.default_22,
			R.drawable.default_23, R.drawable.default_24,
			R.drawable.default_25, R.drawable.default_26,
			R.drawable.default_27, R.drawable.default_28,
			R.drawable.default_29, R.drawable.default_30,
			R.drawable.default_31, R.drawable.default_32,
			R.drawable.default_33, R.drawable.default_34 };

	private static final int[] tiles_christmas = { R.drawable.christmas_1,
			R.drawable.christmas_2, R.drawable.christmas_3,
			R.drawable.christmas_4, R.drawable.christmas_5,
			R.drawable.christmas_6, R.drawable.christmas_7,
			R.drawable.christmas_8, R.drawable.christmas_9,
			R.drawable.christmas_10, R.drawable.christmas_11,
			R.drawable.christmas_12, R.drawable.christmas_13,
			R.drawable.christmas_14, R.drawable.christmas_15,
			R.drawable.christmas_16, R.drawable.christmas_17,
			R.drawable.christmas_18, R.drawable.christmas_19,
			R.drawable.christmas_20, R.drawable.christmas_21,
			R.drawable.christmas_22 };

	private static final int[] tiles_easter = { R.drawable.easter_1,
			R.drawable.easter_2, R.drawable.easter_3, R.drawable.easter_4,
			R.drawable.easter_5, R.drawable.easter_6, R.drawable.easter_7,
			R.drawable.easter_8, R.drawable.easter_9, R.drawable.easter_10,
			R.drawable.easter_11, R.drawable.easter_12, R.drawable.easter_13,
			R.drawable.easter_14 };

	private static final int[] tiles_tux = { R.drawable.tux_1,
			R.drawable.tux_2, R.drawable.tux_3, R.drawable.tux_4,
			R.drawable.tux_5, R.drawable.tux_6, R.drawable.tux_7,
			R.drawable.tux_8, R.drawable.tux_9, R.drawable.tux_10,
			R.drawable.tux_11, R.drawable.tux_12, R.drawable.tux_13,
			R.drawable.tux_14, R.drawable.tux_15, R.drawable.tux_16,
			R.drawable.tux_17, R.drawable.tux_18, R.drawable.tux_19,
			R.drawable.tux_20, R.drawable.tux_21, R.drawable.tux_22,
			R.drawable.tux_23, R.drawable.tux_24, R.drawable.tux_25,
			R.drawable.tux_26, R.drawable.tux_27, R.drawable.tux_28,
			R.drawable.tux_29, R.drawable.tux_30, R.drawable.tux_31,
			R.drawable.tux_32, R.drawable.tux_33 };

	private static final int[][] icons_set = { tiles_christmas, tiles_easter,
			tiles_default, tiles_tux };

	private static final int[] sounds = { R.raw.blop, R.raw.chime,
			R.raw.chtoing, R.raw.tic, R.raw.toc, R.raw.toing, R.raw.toing2,
			R.raw.toing3, R.raw.toing4, R.raw.toing5, R.raw.toing6,
			R.raw.toong, R.raw.tzirlup, R.raw.whiipz };

	private static final int[] not_found_tile_set = {
			R.drawable.not_found_christmas, R.drawable.not_found_easter,
			R.drawable.not_found_default, R.drawable.not_found_tux };
	private Memory mMemory;
	private MemoryGridView mGridView;
	private FairketApiClient mFairketApiClient;

	/**
	 * {@inheritDoc }
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		newGame();

		mFairketApiClient = FairketAppTimeHelper.onCreate(this,
				FAIRKET_APP_PUBLIC_KEY, FAIRKET_LOG_TAG);
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected View getGameView() {
		return mGridView;
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void newGame() {
		int set = PreferencesService.instance().getIconsSet();
		mMemory = new Memory(icons_set[set], sounds, not_found_tile_set[set],
				this);
		mMemory.reset();
		mGridView = (MemoryGridView) findViewById(R.id.gridview);
		mGridView.setMemory(mMemory);
		drawGrid();
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void about() {
		Intent intent = new Intent(this, CreditsActivity.class);
		startActivity(intent);
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void preferences() {
		Intent intent = new Intent(this, PreferencesActivity.class);
		startActivity(intent);
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void onResume() {
		super.onResume();

		mMemory.onResume(PreferencesService.instance().getPrefs());

		drawGrid();

		FairketAppTimeHelper.onResume(mFairketApiClient);

	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void onPause() {
		super.onPause();

		mMemory.onPause(PreferencesService.instance().getPrefs(), mQuit);

		FairketAppTimeHelper.onPause(mFairketApiClient);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		FairketAppTimeHelper.onDestroy(mFairketApiClient);
	}

	/**
	 * {@inheritDoc }
	 */
	public void onComplete(int countMove) {
		int nHighScore = PreferencesService.instance().getHiScore();
		String title = getString(R.string.success_title);
		Object[] args = { countMove, nHighScore };
		String message = MessageFormat
				.format(getString(R.string.success), args);
		int icon = R.drawable.win;
		if (countMove < nHighScore) {
			title = getString(R.string.hiscore_title);
			message = MessageFormat.format(getString(R.string.hiscore), args);
			icon = R.drawable.hiscore;

			PreferencesService.instance().saveHiScore(countMove);
		}
		this.showEndDialog(title, message, icon);
	}

	/**
	 * {@inheritDoc }
	 */
	public void onUpdateView() {
		drawGrid();
	}

	/**
	 * Draw or redraw the grid
	 */
	private void drawGrid() {
		mGridView.update();
	}

}
