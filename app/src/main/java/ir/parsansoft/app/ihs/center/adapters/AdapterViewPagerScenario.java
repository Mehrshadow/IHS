package ir.parsansoft.app.ihs.center.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import ir.parsansoft.app.ihs.center.FragmentCondition;
import ir.parsansoft.app.ihs.center.FragmentDescription;
import ir.parsansoft.app.ihs.center.FragmentEnhanced;
import ir.parsansoft.app.ihs.center.FragmentResult;

public class AdapterViewPagerScenario extends FragmentPagerAdapter
{
	SparseArray<FragmentEnhanced> registeredFragments = new SparseArray<>();
	public String txt1,txt2,txt3;
	public int id = 0;
	public AdapterViewPagerScenario(FragmentManager fm) {
		super(fm);
	}
	@Override
	public int getCount() {
		return 3;
	}
	@Override
	public FragmentEnhanced getItem(int position) {
		switch (position) {
		case 0:
			return FragmentDescription.newInstance(id);
		case 1:
			return new FragmentCondition();
		case 2:
			return new FragmentResult();
		}
		return null;
	}
	@Override
	public CharSequence getPageTitle(int position) {
		
		switch (position) {
		case 2:
			return "Result";
		case 1:
			return "Condition";
		case 0:
			return "Description";
		}
		return super.getPageTitle(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		FragmentEnhanced fragment = (FragmentEnhanced) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
		fragment.setHtml();
        return fragment;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}
	public FragmentEnhanced getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

	
}
