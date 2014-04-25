package domein;

public class Role
	{
		private boolean hasPermissions;

		public Role(boolean hasPermissions)
			{
				this.hasPermissions = hasPermissions;
			}

		public boolean HasPermissions()
			{
				return hasPermissions;
			}

		public void setHasPermissions(boolean hasPermissions)
			{
				this.hasPermissions = hasPermissions;
			}
	}
