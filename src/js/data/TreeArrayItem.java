package js.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import js.stream.BitStream;

public class TreeArrayItem
{
	private static final String TAG = "TreeArrayItem";
	private String name = "";
	private String value = "";

	private List<TreeArrayItem> childs = new ArrayList<TreeArrayItem>();

	public TreeArrayItem(TreeArrayItem item)
	{
		this.name = item.name;
		this.value = item.value;
		this.childs = item.childs;
	}

	public TreeArrayItem(InputStream stream) throws IOException
	{
		name = BitStream.stringFrom(stream);
		value = BitStream.stringFrom(stream);

		int childsCount = BitStream.u128from(stream);

		Log.v(TAG, String.format("new TreeArrayItem: name = %s, value = %s, childs = %d", name, value, childsCount));

		while (childsCount > 0)
		{
			childs.add(new TreeArrayItem(stream));
			childsCount--;
		}
	}

	public TreeArrayItem(String name, String value)
	{
		if (name != null)
		{
			this.name = name;
		}

		if (value != null)
		{
			this.value = value;
		}
	}

	public void writeTo(OutputStream stream) throws IOException
	{
		BitStream.stringTo(stream, name);
		BitStream.stringTo(stream, value);
		BitStream.u128to(stream, childs.size());

		Log.v(TAG, String.format("writeTo: name = %s, value = %s, childs = %d", name, value, childs.size()));

		for (TreeArrayItem child : childs)
		{
			child.writeTo(stream);
		}
	}

	public String name()
	{
		return name;
	}

	public String value()
	{
		return value;
	}

	public void value(String value)
	{
		if (value != null)
		{
			this.value = value;
		}
		else
		{
			this.value = "";
		}
	}

	public List<TreeArrayItem> childs()
	{
		return childs;
	}

	public TreeArrayItem contain(String name)
	{
		for (TreeArrayItem item : childs)
		{
			if (item.name.equals(name))
			{
				return item;
			}
		}

		return null;
	}

	public void find(String name, List<TreeArrayItem> list)
	{
		list.clear();

		for (TreeArrayItem item : childs)
		{
			if (item.name.equals(name))
			{
				list.add(item);
			}
		}
	}

	public List<TreeArrayItem> find(String name)
	{
		List<TreeArrayItem> list = new ArrayList<TreeArrayItem>();

		find(name, list);

		return list;
	}

	public TreeArrayItem setChild(String name, String value)
	{
		TreeArrayItem item = contain(name);

		if (item == null)
		{
			item = new TreeArrayItem(name, value);
			childs.add(item);
		}
		else
		{
			item.value(value);
		}

		return item;
	}

	public String getChild(String name, String defaultValue)
	{
		TreeArrayItem item = contain(name);

		if (item == null)
		{
			return defaultValue;
		}
		else
		{
			return item.value;
		}
	}

	public void remove(TreeArrayItem item)
	{
		if (childs.remove(item) == false)
		{
			for (TreeArrayItem child : childs)
			{
				child.remove(item);
			}
		}
	}
}
